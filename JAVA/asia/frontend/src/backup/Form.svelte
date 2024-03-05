<script>
    import { todos } from './store.js';
    // letで宣言するだけで埋め込みに使用できる
    let todo = ''

    function addTodo() {
        todos.add(todo)
        todo = '';
    }
</script>
<!-- 変数は{}で埋め込める -->
<!-- bind:valueで入力値と変数を連動させられる -->
<input type="text" bind:value={todo} />
<!-- 関数も{}で埋め込める -->
<button on:click={addTodo}>Add</button>