package org.gemoc.execution.engine.io.views.event;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;
import org.gemoc.commons.eclipse.ui.ViewHelper;
import org.gemoc.execution.engine.core.ObservableBasicExecutionEngine;
import org.gemoc.execution.engine.io.SharedIcons;
import org.gemoc.execution.engine.io.views.IMotorSelectionListener;
import org.gemoc.execution.engine.io.views.engine.EnginesStatusView;
import org.gemoc.execution.engine.io.views.event.commands.CommandState;
import org.gemoc.execution.engine.io.views.event.commands.DoInit;
import org.gemoc.execution.engine.io.views.event.commands.StopPlayScenario;
import org.gemoc.execution.engine.io.views.event.commands.StopRecordScenario;
import org.gemoc.execution.engine.io.views.event.commands.UndoInit;
import org.gemoc.execution.engine.io.views.event.filters.AllBindedClockFilter;
import org.gemoc.execution.engine.io.views.event.filters.Filter;
import org.gemoc.execution.engine.io.views.event.filters.LeftBindedClockFilter;
import org.gemoc.execution.engine.io.views.event.filters.NoFilter;
import org.gemoc.execution.engine.io.views.event.scenario.ScenarioManager;
import org.gemoc.execution.engine.io.views.step.LogicalStepsView;
import org.gemoc.execution.engine.scenario.Fragment;
import org.gemoc.gemoc_language_workbench.api.core.EngineStatus.RunStatus;
import org.gemoc.gemoc_language_workbench.api.core.GemocExecutionEngine;

import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.Clock;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.BasicType.Element;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockConstraintSystem;



/**
 * @author lguillem
 * @version 1.6
 */

public class EventManagerView extends ViewPart implements IMotorSelectionListener, Observer {

	public static final String ID = "org.gemoc.execution.engine.io.views.event.EventManagerView";

	private TableViewer _viewer;
	private Composite _parent;
	private ViewContentProvider _contentProvider;
	private EnginesStatusView _enginesStatusView;
	private ObservableBasicExecutionEngine _engine;
	private Map<ObservableBasicExecutionEngine, WrapperCache> _cacheEngineWrapperMap;
	private Map<ObservableBasicExecutionEngine, ScenarioManager> _cacheEngineScenarioMap;
	private WrapperCache _currentWrapperCache;
	private ScenarioManager _currentScenarioManager;
	private Fragment fragment;
	private Filter _strategyFilterSelected;
	private CacheStatus _state;
	/**
	 * the source provider to enable/ disable command handlers.
	 */
	private CommandState _commandStateService;

	private Button _quickFreeClock;
	private Button _quickForceClock_to_tick;
	private Button _quickForceClock_to_notTick;
	private Label _viewStateLabel; 

	/**
	 * Store the 4 possible states of a Clock and a fifth state for advanced control:
	 * <p> - A clock can be forced to tick or not tick in the future;
	 * <br>- A clock not forced can either tick or not tick in the future;
	 * <br>- (new!) A clock can be forced to tick or not during a certain period.
	 */
	public enum ClockStatus 
	{
		NOTFORCED_SET(false, true),
		NOTFORCED_NOTSET(false, false),
		FORCED_SET(true, true),
		FORCED_NOTSET(true, false),
		DELAYED_CONTROL(null, null);

		private Boolean isForced;
		private Boolean tick;

		ClockStatus(Boolean isForced, Boolean tick)
		{
			this.isForced = isForced;
			this.tick = tick;
		}

		public Boolean isForced()
		{
			return isForced;
		}

		public Boolean getTick()
		{
			return tick;
		}
	}

	/**
	 * Store a "mapping" of all declared filters with a name
	 */
	public enum Filters
	{
		ALL("All Clocks", new NoFilter()),
		NO_LEFT_BIND("No Left binded Clocks", new LeftBindedClockFilter()),
		NO_BIND("No binded Clocks", new AllBindedClockFilter());

		private Filter f;
		private String name;

		Filters(String name, Filter f)
		{
			this.name = name;
			this.f = f;
		}

		Filter getFilter()
		{
			return f;
		}

		String getName()
		{
			return name;
		}
	}

	/**
	 * Player and Recorder commands
	 */
	public enum Commands
	{
		STOP_PLAY_SCENARIO(StopPlayScenario.ID),
		STOP_RECORD_SCENARIO(StopRecordScenario.ID),
		DO_INIT(DoInit.ID),
		UNDO_INIT(UndoInit.ID);

		String id;

		Commands(String id)
		{
			this.id = id;
		}

		public String getID()
		{
			return id;
		}
	}

	/**
	 * Controls for the source provider flags to control
	 * wether commands handlers are enabled or not.
	 */
	public enum SourceProviderControls 
	{
		PLAY,
		RECORD,
		INIT,
		RESET;
	}

	public enum CacheStatus
	{
		WAITING("Waiting"),
		RECORDING("Recording"),
		PLAYING("Playing"),
		STOPPED("No Engine selected");

		private String text;

		CacheStatus(String text)
		{
			this.text = text;
		}

		String getText()
		{
			return text;
		}

	}


	/**
	 * The constructor.
	 */
	public EventManagerView() 
	{
		_cacheEngineWrapperMap = new HashMap<ObservableBasicExecutionEngine, WrapperCache>();
		_cacheEngineScenarioMap = new HashMap<ObservableBasicExecutionEngine, ScenarioManager>();
		_currentWrapperCache = null;
		fragment = null;
		_strategyFilterSelected = new NoFilter();
		_commandStateService = null;
		_state = CacheStatus.STOPPED;
		LogicalStepsView decisionView = ViewHelper.<LogicalStepsView>retrieveView(LogicalStepsView.ID);
		decisionView.addSelectionListener(new ISelectionChangedListener() 
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event) 
			{
				updateView();
			}
		});
	}


	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		// The main parent will be made of a single column
		GridLayout layout = new GridLayout();
		_parent = parent;	
		_parent.setLayout(layout);
		// In the first row there will be a 2 column gridLayout
		createFilterSelectionBar();
		// In the second row there will be our table with clocks
		createViewer();
		// Create a selection Listener for buttons and pop up menu
		SelectionListener listener = createSelectionListener();
		// Create a menu which will show when right click is pressed on a row
		createPopUpMenu(listener);
		// Create a display of the eventview status and create the buttons
		createInformationAndButtons(listener);
		// get the view to listen to motor selection
		startListeningToMotorSelectionChange();
	}

	/**
	 * Generate a label and a combo to make the user able to change the filter strategy
	 */
	private void createFilterSelectionBar() 
	{
		// The bar will be placed in the first row of the parent's grid ( which has a single column )
		Composite filterSelectionBar = new Composite(_parent, SWT.BORDER);
		// The bar will be made of 2 parts :
		filterSelectionBar.setLayout(new GridLayout(2, false));
		filterSelectionBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// A label on the first column
		(new Label(filterSelectionBar, SWT.NULL)).setText("Select an event filter: ");
		// A read only combo on the second
		final Combo combo = new Combo(filterSelectionBar, SWT.NULL | SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Filters[] strategyFilters = Filters.values();
		Arrays.sort(strategyFilters);
		// We add all existing filters on our combo
		for(Filters myFilters: strategyFilters)
		{
			// The name of the filter will be displayed on the combo cells
			combo.add(myFilters.getName());
			// We map an instance of the Filter with his name
			combo.setData(myFilters.getName(), myFilters.getFilter());
		}
		combo.addSelectionListener(new SelectionListener() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				// We get the name of the selected strategy
				String filterName = combo.getItem(combo.getSelectionIndex());
				// Thanks to our map we can select the proper instance of Filter
				_strategyFilterSelected = (Filter) combo.getData(filterName);
				// We refresh the view
				updateView();
			}
			//If the combo is clicked but no item selected:
			public void widgetDefaultSelected(SelectionEvent e) 
			{
				String text = combo.getText();
				if(combo.indexOf(text) < 0) { // Not in the list yet 
					combo.add(text);
					// Re-sort
					String[] items = combo.getItems();
					Arrays.sort(items);
					combo.setItems(items);
				}
			}
		});
	}




	/**
	 * Create the tableViewer and all its components like its ContentProvider or its
	 * layout structure.
	 */
	private void createViewer()
	{
		// Define the TableViewer
		_contentProvider = new ViewContentProvider();
		_viewer = new TableViewer(_parent, SWT.BORDER| SWT.MULTI);
		_viewer.setContentProvider(_contentProvider);
		createColumn();
		// make lines and header visible
		final Table table = _viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// The table will take all the horizontal and vertical excess space
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true);
		_viewer.getControl().setLayoutData(grid);
	}

	/**
	 * Create the column(s) of the tableViewer
	 */
	private void createColumn()
	{	
		TableViewerColumn viewerColumn1 = new TableViewerColumn(_viewer, SWT.LEFT);
		TableColumn column1 = viewerColumn1.getColumn();
		column1.setText("Clock");
		column1.setWidth(100);
		column1.setResizable(true);
		column1.setMoveable(true);

		viewerColumn1.setLabelProvider(new ColumnLabelProvider() 
		{
			@Override
			public String getText(Object element) 
			{
				String result = new String();          
				if (element instanceof ClockWrapper)
				{
					Clock c = ((ClockWrapper)element).getClock();
					result = c.getName();
				}
				return result;
			}

			@Override
			public Image getImage(Object element) 
			{
				if (element instanceof ClockWrapper) 
				{
					ClockStatus state = ((ClockWrapper) element).getState();
					switch(state)
					{
					case NOTFORCED_SET: return SharedIcons.getSharedImage(SharedIcons.NOTFORCED_CLOCK_SET);
					case FORCED_SET: return SharedIcons.getSharedImage(SharedIcons.FORCED_CLOCK_SET);
					case NOTFORCED_NOTSET: return SharedIcons.getSharedImage(SharedIcons.NOTFORCED_CLOCK_NOTSET);
					case FORCED_NOTSET: return SharedIcons.getSharedImage(SharedIcons.FORCED_CLOCK_NOTSET);
					default: break;
					}
				}
				return null;
			}

			@Override
			public Color getBackground(Object element) 
			{
				if (element instanceof ClockWrapper)
				{
					ClockStatus state = ((ClockWrapper) element).getState();

					switch(state)
					{
					case NOTFORCED_SET: return new Color(_parent.getDisplay(), 212, 255, 141);
					case FORCED_SET: return new Color(_parent.getDisplay(), 255, 217, 142);
					case FORCED_NOTSET: return new Color(_parent.getDisplay(), 255, 217, 142);
					default: break;
					}
				}
				return super.getBackground(element);
			}
		});
	}

	/**
	 * Create the pop up menu which will be displayed following the right click
	 * operation on a row of the tableViewer.
	 * <br>The user can choose an action to realize on the selected Clock(s).
	 * @param listener
	 */
	private void createPopUpMenu(SelectionListener listener) 
	{
		final Menu menu = new Menu(_parent.getShell(), SWT.POP_UP);
		final Table table = _viewer.getTable();
		table.setMenu(menu);

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Force  to tick");
		item.setData(ClockStatus.FORCED_SET);
		item.addSelectionListener(listener);

		new MenuItem(menu, SWT.SEPARATOR);
		item = new MenuItem(menu, SWT.PUSH);
		item.setText("Force  to not tick");
		item.setData(ClockStatus.FORCED_NOTSET);
		item.addSelectionListener(listener);

		new MenuItem(menu, SWT.SEPARATOR);
		item = new MenuItem(menu, SWT.PUSH);
		item.setText("Free the clock(s)");
		item.setData(ClockStatus.NOTFORCED_NOTSET);
		item.addSelectionListener(listener);

		new MenuItem(menu, SWT.SEPARATOR);
		item = new MenuItem(menu, SWT.PUSH);
		item.setText("Advanced settings");
		item.setData(ClockStatus.DELAYED_CONTROL);
		item.addSelectionListener(listener);
	}

	public void createInformationAndButtons(SelectionListener listener)
	{
		// The bar will be placed in the first row of the parent's grid ( which has a single column )
		Composite informationBar = new Composite(_parent, SWT.BORDER);
		// The bar will be made of 3 parts :
		informationBar.setLayout(new GridLayout(3, false));
		informationBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		informationBar.setBackground(new Color(_parent.getDisplay(), 255, 255, 255));
		//An icon on the first column
		Label icon = new Label(informationBar, SWT.IMAGE_PNG);
		icon.setImage(SharedIcons.getSharedImage(SharedIcons.ENGINE_ICON));
		// A label on the second column
		_viewStateLabel = new Label(informationBar, SWT.NULL);
		// 3 buttons on the third column
		Composite buttonBar = new Composite(informationBar, SWT.NONE);
		buttonBar.setLayout(new GridLayout(3, false));
		buttonBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// Button which will freed the selected Clocks
		_quickFreeClock = new Button(buttonBar, SWT.PUSH);
		_quickFreeClock.setImage(SharedIcons.getSharedImage(SharedIcons.NOTFORCED_CLOCK_NOTSET));
		_quickFreeClock.setToolTipText("Free");
		_quickFreeClock.setData(ClockStatus.NOTFORCED_NOTSET);
		_quickFreeClock.setLayoutData(new GridData(SWT.END,SWT.NONE,true, false));
		// Button which will forced to not tick the selected Clocks
		_quickForceClock_to_notTick = new Button(buttonBar, SWT.PUSH);
		_quickForceClock_to_notTick.setImage(SharedIcons.getSharedImage(SharedIcons.FORCED_CLOCK_NOTSET));
		_quickForceClock_to_notTick.setToolTipText("Force to not tick");
		_quickForceClock_to_notTick.setData(ClockStatus.FORCED_NOTSET);
		_quickForceClock_to_notTick.setLayoutData(new GridData(SWT.END,SWT.NONE,false, false));
		// Button which will forced to tick the selected Clocks
		_quickForceClock_to_tick = new Button(buttonBar, SWT.PUSH);
		_quickForceClock_to_tick.setImage(SharedIcons.getSharedImage(SharedIcons.FORCED_CLOCK_SET));
		_quickForceClock_to_tick.setToolTipText("Force to tick");
		_quickForceClock_to_tick.setData(ClockStatus.FORCED_SET);
		_quickForceClock_to_tick.setLayoutData(new GridData(SWT.END,SWT.NONE,false, false));

		_quickFreeClock.addSelectionListener(listener);
		_quickForceClock_to_notTick.addSelectionListener(listener);
		_quickForceClock_to_tick.addSelectionListener(listener);

		updateInformationAndButtons();
	}

	public SelectionListener createSelectionListener()
	{
		final Table table = _viewer.getTable();

		SelectionListener listener = new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				List<String> clockToForce = new ArrayList<String>();
				ClockStatus state = ClockStatus.NOTFORCED_NOTSET;
				for(TableItem item : table.getSelection())
				{
					clockToForce.add(item.getText());
				}

				if(e.getSource() instanceof MenuItem)
				{
					state = (ClockStatus) ((MenuItem)e.getSource()).getData();
				}
				if(e.getSource() instanceof Button)
				{
					state = (ClockStatus) ((Button)e.getSource()).getData();
				}

				forceClock(clockToForce, state);	
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		return listener;
	}

	/**
	 * Force or Free a list of clocks
	 * @param clockToForce The clock list to force or free
	 * @param state The future state of the clock(s) to force
	 */
	private void forceClock(List<String> clockToForce, ClockStatus state) 
	{
		switch(state)
		{
		case DELAYED_CONTROL: 
			AdvancedControlDialog dialog = new AdvancedControlDialog(_parent.getShell());
			dialog.create();
			dialog.open();
			break;
		default:
			for(String clockName : clockToForce)
			{
				ClockWrapper wrapper  = _currentWrapperCache.getClockWrapper(clockName);
				if( ! (wrapper.getState().equals(ClockStatus.NOTFORCED_SET) && state.equals(ClockStatus.NOTFORCED_NOTSET)))
				{
					wrapper.setState(state);
				}
			}
			updateView();
			break;	
		}
	}

	/**
	 * Refresh the input of the ContentProvider with the selected strategy
	 */
	public void updateView()
	{
		Display.getDefault().asyncExec(new Runnable() 
		{
			@Override
			public void run() 
			{
				setViewBehaviorForCurrentEngine();
				if(!_state.equals(CacheStatus.STOPPED))
				{
					_currentWrapperCache.refreshFutureTickingFreeClocks();
					_contentProvider.setFilterStrategy(_strategyFilterSelected);
				}
				updateInformationAndButtons();
				_viewer.setInput(_currentWrapperCache);
			}				
		});
	}

	/**
	 * Enable or disable commands handlers in function of the current cache state of a selected engine.
	 */
	private void setViewBehaviorForCurrentEngine()
	{
		switch(_state)
		{
		case WAITING: // engine running without recording or playing a fragment
			_commandStateService.setPlayDisabled();
			_commandStateService.setRecordDisabled();
			_commandStateService.setInit(); 
			break;
		case PLAYING: // engine running and playing a fragment
			_commandStateService.setInit();
			_commandStateService.setRecordDisabled();
			_commandStateService.setPlayEnabled();
			break;
		case RECORDING: // engine running and recording a fragment
			_commandStateService.setInit();
			_commandStateService.setPlayDisabled();
			_commandStateService.setRecordEnabled();
			break;
		case STOPPED: // engine stopped
			_commandStateService.setPlayDisabled();
			_commandStateService.setRecordDisabled();
			_commandStateService.resetInit();
			break;
		default: break;	
		}
	}

	/**
	 * Initialize the cache of wrappers for the current engine.
	 */
	private void createWrapperCacheForEngine() 
	{
		ClockConstraintSystem system = extractSystem();
		_currentWrapperCache = new WrapperCache(_engine);

		if (system != null)
		{
			for(Element e : system.getSubBlock().get(0).getElements())
			{
				if (e instanceof Clock)
				{
					_currentWrapperCache.add((Clock)e);
				}
			}
		}
		_currentWrapperCache.configure(_engine, system);
		_cacheEngineWrapperMap.put(_engine, _currentWrapperCache);
	}

	/**
	 * Initialize the cache of ScenarioManager for the current engine.
	 */
	private void createScenarioCacheForEngine(){
		_currentScenarioManager = new ScenarioManager();
		_currentScenarioManager.setCache(_currentWrapperCache);
		_cacheEngineScenarioMap.put(_engine, _currentScenarioManager);
	}


	private ClockConstraintSystem extractSystem() 
	{
		ClockConstraintSystem system = null;
		for(Resource r : _engine.getExecutionContext().getResourceModel().getResourceSet().getResources())
		{
			if(r.getContents().get(0) instanceof ClockConstraintSystem)
			{
				system = (ClockConstraintSystem)r.getContents().get(0);
				break;
			}
		}
		return system;
	}

	/**
	 * Listen the engine change of state.
	 */
	@Override
	public void update(Observable o, Object arg) 
	{
		int engineStep = (int)_engine.getEngineStatus().getNbLogicalStepRun();
		_state = _currentScenarioManager.getState();
		//We check when the engine reach a new step
		if(engineStep > _currentScenarioManager.getCacheStep())
		{
			if(!_state.equals(CacheStatus.STOPPED))
			{
				if(_state.equals(CacheStatus.RECORDING))
				{	
					recordScenario();
				}
				// The View step is set to the engine new step
				_currentScenarioManager.setcacheStep(engineStep);
				if(_state.equals(CacheStatus.PLAYING))
				{
					playScenario();
				}
			}
			updateView();
		}
	}



	/**
	 * Listen the engine selection in the enginesStatusView
	 */
	@Override
	public void motorSelectionChanged(GemocExecutionEngine engine) {
		if (engine != null) 
		{
			// Cast on engine to access the clockController
			_engine = (ObservableBasicExecutionEngine) engine;
			// if the selected engine is stopped we clean its cache and disable all commands
			if(engine.getEngineStatus().getRunningStatus().equals((RunStatus.Stopped)))
			{
				if(_cacheEngineScenarioMap.get(_engine)!=null){
					_state = _cacheEngineScenarioMap.get(_engine).getState();

					if(_state.equals(CacheStatus.RECORDING))
					{
						executeCommand(Commands.STOP_RECORD_SCENARIO);
					}
					if(_state.equals(CacheStatus.PLAYING))
					{
						executeCommand(Commands.STOP_PLAY_SCENARIO);
					}
				}
				_state = CacheStatus.STOPPED;
				_engine.deleteObserver(this);
				_currentWrapperCache = null;
				executeCommand(Commands.UNDO_INIT);
			}
			else // else we set the current state according to the selected engine cache state
			{
				_engine.deleteObserver(this);
				_engine.addObserver(this);
				executeCommand(Commands.DO_INIT);

				if(_cacheEngineWrapperMap.get(_engine) == null)
				{
					createWrapperCacheForEngine();
					createScenarioCacheForEngine();
				}
				_currentWrapperCache = _cacheEngineWrapperMap.get(_engine);
				_currentScenarioManager = _cacheEngineScenarioMap.get(_engine);
				_state = _currentScenarioManager.getState();
			}
			updateView();
		}
	}


	/**
	 * Call the execute function of a command.
	 * @param command : The name of the command to be called
	 */
	public void executeCommand(Commands command){
		IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
		try 
		{
			handlerService.executeCommand(command.getID(), null);
		} 
		catch (Exception ex) 
		{
			throw new RuntimeException(command.getID() + " command not found");
		}
	}




	private void startListeningToMotorSelectionChange() {
		_enginesStatusView = ViewHelper.retrieveView(EnginesStatusView.ID);
		if (_enginesStatusView != null) 
		{
			_enginesStatusView.addMotorSelectionListener(this);
		}
	}

	private void stopListeningToMotorSelectionChange() {
		if (_enginesStatusView != null) 
		{
			_enginesStatusView.removeMotorSelectionListener(this);
		}
	}


	@Override
	public void dispose() {
		super.dispose();
		_contentProvider.dispose();
		if(_engine != null)
		{
			_engine.deleteObserver(this);
		}
		_engine = null;
		stopListeningToMotorSelectionChange();
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() 
	{
		_viewer.getTable().setFocus();
	}

	public void startRecordScenario() 
	{
		_currentScenarioManager.startRecord();
		_state = _currentScenarioManager.getState();
		updateView();

	}

	public void recordScenario()
	{
		_currentScenarioManager.record();
	}

	public void stopRecordScenario()
	{
		_currentScenarioManager.stopRecord();
		_state = _currentScenarioManager.getState();
		updateView();	
	}

	/**
	 * If the path is correct, the scenario is loaded.
	 * @param path
	 */
	public void loadScenario(String path)
	{
		if(path != null)
		{
			_currentScenarioManager.load(path);
			if(fragment != null)
			{
				_state = _currentScenarioManager.getState();
				updateView();
			}
			playScenario();	
		}
	}

	/**
	 * While the scenario possess execution step to be done, playScenario() is called.
	 * When the last step has been played, we stop the replay.
	 */
	public void playScenario()
	{
		if(fragment != null)
		{
			if(_currentScenarioManager.getProgress() < fragment.getStepList().size())
			{
				_currentScenarioManager.play();
			}
			else
			{
				executeCommand(Commands.STOP_PLAY_SCENARIO);
			}
		}
	}

	public void stopPlayScenario()
	{
		_currentScenarioManager.stop();
		_state = _currentScenarioManager.getState();
		_currentWrapperCache.freeAllClocks();
	}

	public void informationMsg(final String title, final String msg){
		final EventManagerView eventView = this;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(eventView.getSite().getShell(), title, msg);
			}				
		});
	}

	/**
	 * Set or reset variables in the SourceProvider to enable or disable command's handlers 
	 * (to make them appear grayed).
	 * The mapping is done in the plugin.xml
	 * @param event
	 * @param command
	 */
	public void executeService(ExecutionEvent event, SourceProviderControls command)
	{
		// Get the source provider service
		ISourceProviderService sourceProviderService = (ISourceProviderService) HandlerUtil
				.getActiveWorkbenchWindow(event).getService(ISourceProviderService.class);
		// now get my service
		_commandStateService = (CommandState) sourceProviderService
				.getSourceProvider(CommandState.ID);
		switch(command)
		{
		case PLAY: _commandStateService.setPlayEnabled(); break;
		case RECORD: _commandStateService.setRecordEnabled(); break;
		case INIT: _commandStateService.setInit(); break;
		case RESET: _commandStateService.resetInit();	
		break;
		default: break;
		}
	}

	public void setScenario(Fragment fragment)
	{
		this.fragment = fragment;
	}

	public Fragment getScenario()
	{
		return fragment;
	}

	public GemocExecutionEngine getEngine() 
	{
		return _engine;
	}

	public void updateInformationAndButtons()
	{
		_quickFreeClock.setEnabled(_engine!=null);
		_quickForceClock_to_notTick.setEnabled(_engine!=null);
		_quickForceClock_to_tick.setEnabled(_engine!=null);

		_viewStateLabel.setText(_state.getText()+" ...");
	}	
}