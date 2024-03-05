import App from './App.svelte';

const app = new App({
    // ここでbodyを指定しているから特にdivとかを書かなくて良い
    
    target: document.body,
});

export default app;