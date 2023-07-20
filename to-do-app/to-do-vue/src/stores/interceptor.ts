import { useUserStore } from "./user_store"


export const sendHttpRequest = (url: string, method: string, body: string) => {

    const userStore = useUserStore();
    let header: {} = { 'Content-Type': 'application/json' };
    if (userStore.isUserLoggedIn) {
        header = { 'Content-Type': 'application/json', 'AUTHORIZATION': 'Bearer ' + userStore.user!.jwt };
    }


    if (body.trim().length > 0) {
        return fetch(url, { method: method, headers: header, body: body })
    }
    return fetch(url, { method: method, headers: header })

}