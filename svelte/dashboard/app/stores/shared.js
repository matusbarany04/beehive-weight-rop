import { writable } from "svelte/store";

const user = writable({});

export default {
    async initAll() {
        try {
            let response = await fetch("/user/byToken");
            if (response.ok) {
                const userData = await response.json();
                user.set(userData.user);
            } else {
                console.error('Failed to fetch user:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching user:', error);
        }
    },
    getUser() {
        let userValue;
        user.subscribe(value => {
            userValue = value;
        })();
        return userValue;
    }
}