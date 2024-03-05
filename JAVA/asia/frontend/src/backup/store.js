import { writable } from 'svelte/store';

// TODOリストの初期リスト（空のリスト）
const initial = []

// storeをカスタマイズする
function createTodos() {
    const { subscribe, set, update } = writable(initial);

    return {
        subscribe,
        add: (todo) => update(todos => todos.concat(todo)),
        reset: set(initial)
    };
}
export const todos = createTodos();