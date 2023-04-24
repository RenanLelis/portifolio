import { defineStore } from "pinia"

export const useUserStore = defineStore('task', () => {

    const fetchTasksAndLists = () => {
        //TODO
    }

    const fetchTasksByList = (idList: number) => {
        //TODO
    }

    const createTaskList = (listName: string, listDescription: string) => {
        //TODO
    }

    const updateTaskList = (listName: string, listDescription: string, idList: Number) => {
        //TODO
    }

    const deleteTaskList = (idList: Number) => {
        //TODO
    }
   

    return {
        fetchTasksAndLists,
        fetchTasksByList,
        createTaskList,
        updateTaskList,
        deleteTaskList,
        
    }
})