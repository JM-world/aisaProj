<script>
    export let y;

    let tabs = [
        { name: "About Project", link: "#projects" },
        { name: "Nudge X Public Data", link: "#nudge" },
        { name: "Spring X Maria DB", link: "#spring" },
        { name: "GPT-4 X FAST API", link: "#gpt4" },
        // {name: 'Projecs', '#projects'}
    ];
</script>

<header
    class={"sticky z-[10] top-0 duration-200 px-6 flex items-center justify-between border border-solid " +
        (y > 0
            ? " py-4 bg-slate-950 border-violet-950"
            : " py-4 bg-slate-950 border-transparent")}
>
    <a href="#introPage">
        <h1 class="font-medium">
            <b class="font-bold poppins">ASIA Edu X</b> LG U+
        </h1>
    </a>
    <div class="sm:flex ml-auto pr-4 items-center gap-4 hidden">
        {#each tabs as tab, index}
            <a href={tab.link} class="duration-200 hover:text-violet-400">
                <p>{tab.name}</p>
            </a>
        {/each}
    </div>
    <a
        href="/admin/login"
        target="_blank"
        class="blueShadow relative overflow-hidden px-5 py-2 group rounded-full bg-white text-slate-950"
    >
        <div
            class="absolute top-0 right-full w-full h-full bg-violet-400 opacity-20 group-hover:translate-x-full z-0 duration-200"
        ></div>
        <h4 class="relative z-9">Go to Admin</h4>
    </a>
</header>
