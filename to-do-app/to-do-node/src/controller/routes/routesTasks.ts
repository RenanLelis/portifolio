import express from 'express';
import taskListController from '../taskListController';
import mid from '../middleware/middleware'

const router = express.Router();

router.get('/api/taskList', mid.validateAuth, taskListController.getTasksAndLists)
router.get('/api/list', mid.validateAuth, taskListController.getLists)
router.get('/api/taskList/tasks/:id', mid.validateAuth, taskListController.getTasksByList)
router.post('/api/taskList', mid.validateAuth, taskListController.createTaskList)
router.put('/api/taskList/:id', mid.validateAuth, taskListController.updateTaskList)
router.delete('/api/taskList/:id', mid.validateAuth, taskListController.deleteTaskList)
router.put('/api/taskList/tasks/complete/:id', mid.validateAuth, taskListController.completeTasksFromList)
router.put('/api/taskList/tasks/uncomplete/:id', mid.validateAuth, taskListController.uncompleteTasksFromList)
router.put('/api/taskList/tasks/moveFromList', mid.validateAuth, taskListController.moveTasksFromList)
router.post('/api/task', mid.validateAuth, taskListController.createTask)
router.put('/api/task/:id', mid.validateAuth, taskListController.updateTask)
router.delete('/api/task/:id', mid.validateAuth, taskListController.deleteTask)
router.put('/api/task/complete/:id', mid.validateAuth, taskListController.completeTask)
router.put('/api/task/uncomplete/:id', mid.validateAuth, taskListController.uncompleteTask)
router.put('/api/task/list/:id', mid.validateAuth, taskListController.moveTaskToList)

export default { router };