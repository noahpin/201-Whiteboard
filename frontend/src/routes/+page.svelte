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
            id: "csci-201",
            title: "CSCI 201 Planning Board",
            author: "Noah Pinales",
            updated: "12:53 pm",
            date: null,
        },
        {
            id: "ee-250",
            title: "EE 250 Notes",
            author: "Noah Pinales",
            updated: "12:53 pm",
            date: null,
        },
        {
            id: "board-3",
            title: "Board board board",
            author: "Noah Pinales",
            updated: "8:00 pm",
            date: null,
        },
        {
            id: "board-2",
            title: "Board Board",
            author: "Noah Pinales",
            updated: "12:53 pm",
            date: null,
        },
    ];
</script>

<div class="whiteboards-page container">
    <div class="header">
        <div class="left-group">
            <h1>Whiteboards</h1>
            <button class="create-btn">CREATE WHITEBOARD</button>
        </div>
        <div class="username">Username</div>
        <div class="left-group">
            <h1>Whiteboards</h1>
            <button class="create-btn">CREATE WHITEBOARD</button>
        </div>
        <div class="username">Username</div>
    </div>

    <div class="whiteboard-list">
        {#each whiteboards as board}
            <a class="whiteboard-entry" href={`/whiteboard/${board.id}`}>
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
            </a>
        {/each}
    </div>
</div>

<style>
    .whiteboards-page {
        padding: 2rem;
        font-family: sans-serif;
    }
    
    .container {
        max-width: 70vw;
        margin: 0 auto;
    }

    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.5rem;
    }

    .left-group {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .username {
        font-size: 1rem;
        color: #666;
    }

    .left-group {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .username {
        font-size: 1rem;
        color: #666;
    }

    .header h1 {
        font-size: 2rem;
        font-weight: 400;
    }

    .create-btn {
        background-color: #e2e2e2;
        border: none;
        padding: 0.4rem 0.8rem;
        border-radius: 8px;
        font-weight: 800;
        font-size: 0.9rem;
        cursor: pointer;
    }

    .create-btn:hover {
        background-color: #d0d0d0;
    }

    .whiteboard-list {
        display: flex;
        flex-direction: column;
        border-bottom: 1px solid #e0e0e0;
    }

    .whiteboard-entry {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        border-top: 1px solid #e0e0e0;
        padding: 0.75rem 0;
        font-size: 1rem;
        flex-wrap: wrap;
        text-decoration: none;
        color: inherit;
    }

    .whiteboard-entry:hover {
        background-color: #f9f9f9;
        cursor: pointer;
    }

    .info {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
        align-items: baseline;
    }

    .title {
        font-weight: 500;
        padding-left: 1rem;
    }

    .author {
        color: #888;
        font-size: 0.95rem;
        font-weight: 400;
        }

    .updated {
        font-size: 0.9rem;
        font-size: 0.9rem;
        color: #666;
    }
</style>

