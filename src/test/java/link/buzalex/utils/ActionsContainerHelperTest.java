package link.buzalex.utils;

import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.Action;
import link.buzalex.models.actions.FinishStepAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ActionsContainerHelperTest {

    private ActionsContainer stepActions;
    private ActionsContainerHelper helper;

    @BeforeEach
    void setUp() {
        // Initialize a real ActionsContainer instance
        stepActions = new ActionsContainer();
        helper = new ActionsContainerHelper(stepActions);
    }

    @Test
    void testConstructorInitializesActionNames() {
        // Test that action names are initialized correctly in the constructor
        ActionsContainer container = new ActionsContainer();
        container.setName("action1");
        container.setAction(new FinishStepAction(null));

        ActionsContainerHelper initializedHelper = new ActionsContainerHelper(container);
        Set<String> actionNames = initializedHelper.getActionNames();

        assertTrue(actionNames.contains("action1"), "Constructor should initialize action names correctly");
    }

    @Test
    void testPutActionSetsNewActionIfNoneExists() {
        // Test that the action and name are set if stepActions has no action
        Action action = new FinishStepAction(null);
        String methodName = "testMethod";

        helper.putAction(action, methodName);

        assertEquals(methodName, stepActions.getName(), "Name should be set to methodName if no action exists");
        assertEquals(action, stepActions.getAction(), "Action should be set if no action exists");
    }

    @Test
    void testPutActionAppendsToEndIfActionExists() {
        // Test that the action is appended if stepActions already has an action
        Action firstAction = new FinishStepAction(null);
        stepActions.setAction(firstAction);
        stepActions.setName("initialMethod");

        Action newAction = new FinishStepAction(null);
        String newMethodName = "newMethod";

        helper.putAction(newAction, newMethodName);

        assertNotNull(stepActions.getNextAction(), "Next action should be created if an action already exists");
        assertEquals(newMethodName, stepActions.getNextAction().getName(), "New action's name should be set to newMethod");
        assertEquals(newAction, stepActions.getNextAction().getAction(), "New action should be appended to the end");
    }

    @Test
    void testPutActionWithMultipleActionsInChain() {
        // Test adding multiple actions to ensure correct chaining
        Action firstAction = new FinishStepAction(null);
        Action secondAction = new FinishStepAction(null);
        Action thirdAction = new FinishStepAction(null);

        helper.putAction(firstAction, "firstMethod");
        helper.putAction(secondAction, "secondMethod");
        helper.putAction(thirdAction, "thirdMethod");

        assertEquals("firstMethod", stepActions.getName());
        assertEquals(firstAction, stepActions.getAction());
        assertNotNull(stepActions.getNextAction(), "Second action should be added as next action");

        ActionsContainer secondContainer = stepActions.getNextAction();
        assertEquals("secondMethod", secondContainer.getName());
        assertEquals(secondAction, secondContainer.getAction());
        assertNotNull(secondContainer.getNextAction(), "Third action should be added as next action to the second");

        ActionsContainer thirdContainer = secondContainer.getNextAction();
        assertEquals("thirdMethod", thirdContainer.getName());
        assertEquals(thirdAction, thirdContainer.getAction());
        assertNull(thirdContainer.getNextAction(), "Third action should be the last in the chain");
    }

    @Test
    void testActionNamesContainAllActionsInChain() {
        // Test that action names set contains names of all actions in the chain
        Action action1 = new FinishStepAction(null);
        Action action2 = new FinishStepAction(null);

        helper.putAction(action1, "action1");
        helper.putAction(action2, "action2");

        Set<String> actionNames = helper.getActionNames();

        assertTrue(actionNames.contains("action1"), "actionNames should contain the first action's name");
        assertTrue(actionNames.contains("action2"), "actionNames should contain the second action's name");
    }

    @Test
    void testPutActionWithMultipleActionsInChainWithSameNames() {
        // Test adding multiple actions to ensure correct chaining
        Action firstAction = new FinishStepAction(null);
        Action secondAction = new FinishStepAction(null);
        Action thirdAction = new FinishStepAction(null);

        helper.putAction(firstAction, "firstMethod");
        helper.putAction(secondAction, "firstMethod");
        helper.putAction(thirdAction, "firstMethod");

        assertEquals("firstMethod", stepActions.getName());
        assertEquals(firstAction, stepActions.getAction());
        assertNotNull(stepActions.getNextAction(), "Second action should be added as next action");

        ActionsContainer secondContainer = stepActions.getNextAction();
        assertEquals("firstMethod#1", secondContainer.getName());
        assertEquals(secondAction, secondContainer.getAction());
        assertNotNull(secondContainer.getNextAction(), "Third action should be added as next action to the second");

        ActionsContainer thirdContainer = secondContainer.getNextAction();
        assertEquals("firstMethod#2", thirdContainer.getName());
        assertEquals(thirdAction, thirdContainer.getAction());
        assertNull(thirdContainer.getNextAction(), "Third action should be the last in the chain");
    }

    @Test
    void testPutActionWithNullAction() {
        // Test that putting a null action does not throw an error and does not alter chain
        String methodName = "nullMethod";

        helper.putAction(null, methodName);

        assertEquals(0, helper.getActionNames().size(), "Null action should not be added to the actionNames set");
        assertNull(stepActions.getAction(), "Step actions should remain null if a null action is provided");
    }
}
