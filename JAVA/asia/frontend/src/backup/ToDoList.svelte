<script>
    import { todos } from './store.js';

    let todoList = []
    // storeの値を読み込む場合はsubscribeで実装
    todos.subscribe(value => {
        todoList = value;
    })
</script>

<ul>
<!-- ループの記述の順番に注意！　 #each <配列> as <要素>, <番号>  -->
    {#each todoList as item, index}
        <li>{item}</li>
    {/each}
</ul>