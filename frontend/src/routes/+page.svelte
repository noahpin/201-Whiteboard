<!-- 
 
 This page will be the main whiteboards page, and will list 
 all whiteboards that a user has edited.

 Auth check will be in this pages server file, and will
 redirect to login if the user is not authenticated.
 
 -->

 <script>
    // Example Whiteboard Array with Dummy Values
    let whiteboards = [
        {
            title: "CSCI 201 Planning Board",
            author: "Noah Pinales",
            updated: "12:53 pm",
            date: null,
        },
        {
            title: "EE 250 Notes",
            author: "Noah Pinales",
            updated: "12:53 pm",
            date: null,
        },
        {
            title: "Board board board",
            author: "Noah Pinales",
            updated: "8:00 pm",
            date: null,
        },
        {
            title: "Board Board",
            author: "Noah Pinales",
            updated: "12:53 pm",
            date: null,
        },
    ];
</script>

<div class="whiteboards-page">
    <div class="header">
        <h1>Whiteboards</h1>
        <button class="create-btn">CREATE WHITEBOARD</button>
    </div>

    <div class="whiteboard-list">
        {#each whiteboards as board}
            <div class="whiteboard-entry">
                <div class="info">
                    <div class="title">{board.title}</div>
                    <div class="author">By: {board.author}</div>
                </div>
                <div class="updated">
                    {#if board.updated}
                        Updated at {board.updated}
                    {:else}
                        Updated {board.date}
                    {/if}
                </div>
            </div>
        {/each}
    </div>
</div>

<style>
    .whiteboards-page {
        padding: 2rem;
        font-family: sans-serif;
    }

    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.5rem;
    }

    .header h1 {
        font-size: 1.75rem;
        font-weight: 600;
    }

    .create-btn {
        background-color: #e2e2e2;
        border: none;
        padding: 0.5rem 1rem;
        border-radius: 4px;
        font-weight: 500;
        cursor: pointer;
    }

    .create-btn:hover {
        background-color: #d0d0d0;
    }

    .whiteboard-list {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }

    .whiteboard-entry {
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid #e0e0e0;
        padding-bottom: 0.75rem;
    }

    .info .title {
        font-weight: 500;
    }

    .info .author {
        font-size: 0.875rem;
        color: #666;
    }

    .updated {
        font-size: 0.875rem;
        color: #666;
        align-self: center;
    }
</style>

