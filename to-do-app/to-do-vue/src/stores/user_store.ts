import { ref, computed, type Ref } from 'vue'
import { defineStore } from 'pinia'
import type { User } from '@/model/user'
import { URL_UPDATE_PROFILE, USER_DATA } from '@/consts/consts';

export const useUserStore = defineStore('user', () => {
   

    const user: Ref<User | null> = ref(null);

    const isUserLoggedIn = computed(() => {
        return (
            user.value !== null &&
            user.value.jwt !== null &&
            user.value.jwt.length > 0 &&
            user.value.tokenExpirationDate &&
            user.value.tokenExpirationDate > new Date().getTime()) as boolean;
    })

    const loadUserInMemory = () => {
        if (user.value === null) {
            const data = sessionStorage.getItem(USER_DATA)
            if (data !== null) {
                const loadedUser: User = JSON.parse(data);
                user.value = loadedUser;
            }
        }
    }

    const updateUserProfile = (firstName: string, lastName: string) => {
        return new Promise((resolve, reject) => {
            let headers = {}
            if ( isUserLoggedIn.value ) { headers = {'Content-Type': 'application/json', 'AUTH': user.value!.jwt} }
            else { headers = {'Content-Type': 'application/json'} }
            fetch(URL_UPDATE_PROFILE, {
                method: 'POST',
                body: JSON.stringify({ "firstName": firstName, "lastName": lastName }),
                headers: headers
            }).then((res) => {
                console.log(res);
                // if (res.error) {
                //     return reject(res.error);
                // }
                // res.json().then((data) => {
                //     if (data.erro && data.erro.length > 0) {
                //         return reject(data.erro);
                //     } else {
                //         this.tratarRetornoAutenticacao(data)
                //         return resolve(data);
                //     }
                // })
            }, (error => {
                console.log(error);
                return reject(error);
            }))
        })
    }

    // updateUserProfile(firstName: string, lastName: string) {
    //     let headers: HttpHeaders = new HttpHeaders({
    //       "Content-Type": "application/json",
    //     });
    //     return this.http.put(this.URL_UPDATE_PROFILE, { "firstName": firstName, "lastName": lastName }, { headers });
    //   }

    loadUserInMemory();

    return { 
        user, 
        isUserLoggedIn, 
        updateUserProfile,
    }
})
