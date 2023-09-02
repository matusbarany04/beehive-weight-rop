import { writable } from 'svelte/store';

export const route = writable(window.location.pathname);

// Listen to popstate events, which are fired when navigating with the browser's back/forward buttons
window.addEventListener('popstate', () => {
  route.set(window.location.pathname);
});

// Function to navigate programmatically
export function navigate(path) {
  window.history.pushState({}, '', path);
  route.set(path);
}