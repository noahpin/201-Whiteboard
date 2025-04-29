<!-- 
 
 This page will be the main whiteboards page, and will list 
 all whiteboards that a user has edited.

 Auth check will be in this pages server file, and will
 redirect to login if the user is not authenticated.
 
 -->

<script>
    import { onMount } from "svelte";
    import { PUBLIC_LOCALHOST_URL } from "$env/static/public";
    import { deleteCookie, getCookie } from "svelte-cookie";
    import { goto } from "$app/navigation";

    // Example Whiteboard Array with Dummy Values
    let whiteboards = $state([]);

    let userId = "1"; //dummy for now lol
    let username = $state("");

    onMount(() => {
        userId = getCookie("userId");
        if (
            userId === undefined ||
            userId === null ||
            userId === "-1" ||
            userId === ""
        ) {
            console.log("User ID not found in cookies");
            goto("/login");
            return;
        }
        // Fetch whiteboards from the server
        console.log(PUBLIC_LOCALHOST_URL);
        console.log(
            `${PUBLIC_LOCALHOST_URL}/whiteboard201/whiteboards/get?userId=${userId}`,
        );
        fetch(
            `${PUBLIC_LOCALHOST_URL}/whiteboard201/whiteboards/get?userId=${userId}`,
        )
            .then((response) => response.json())
            .then((data) => {
                whiteboards = data.map((board) => {
                    return {
                        id: board.whiteboardId,
                        title: board.boardName,
                        author: board.username,
                        updated: board.updatedAt.replace("?", " "),
                    };
                });
                console.log(whiteboards, data);
                // whiteboards = data;
            })
            .catch((error) => {
                console.error("Error fetching whiteboards:", error);
            });
        fetch(`${PUBLIC_LOCALHOST_URL}/whiteboard201/username/get?userId=${userId}`)
            .then((res) => res.json())
            .then((data) => {
                if(data.username) {
                    username = data.username;
                }
                else {
                    console.warn("username not found");
                }
            })
            .catch((err) => {
                console.error("failed to fetch username", err);
            });
    });

    async function createWhiteboard() {
        const boardName = prompt("Enter whiteboard name:");
        if (!boardName) {
            alert("Whiteboard creation canceled.");
            return;
        }

        try {
            const response = await fetch(
                `${PUBLIC_LOCALHOST_URL}/whiteboard201/whiteboard/create?userId=${userId}&name=${encodeURIComponent(boardName)}`,
                {
                    method: "GET",
                },
            );

            const result = await response.json();

            if (result.message === "Success") {
                alert("Whiteboard created! Refreshing list...");
                location.reload(); // Or refetch whiteboards in-place
            } else {
                console.error("Create failed:", result.message);
                alert("Failed to create whiteboard.");
            }
        } catch (error) {
            console.error("Network error:", error);
            alert("Error creating whiteboard.");
        }
    }
</script>

<div class="whiteboards-page container">
    <div class="header">
        <div class="left-group">
            <h1>Whiteboards</h1>
            <button class="create-btn" on:click={createWhiteboard}
                >CREATE WHITEBOARD</button
            >
        </div>
        <div class="username">{username}</div>
        <button
            on:click={() => {
                deleteCookie("userId");
                goto("/login");
            }}>Log Out</button
        >
    </div>

    <div class="whiteboard-list">
        {#each whiteboards as board}
            <a class="whiteboard-entry" href={`/edit-whiteboard/${board.id}`}>
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
    :root {
        --primary: #ffc0cb;
        --primary-hover: #f8b0bd;
        --text: #333;
        --muted: #777;
        --bg: #fff0f5;
    }

    .whiteboards-page {
        color:#ffe6ec;
        padding: 2rem;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: var(--bg);
    }


    .container {
        max-width: 70vw;
        height: 100%;
        margin: 0 auto;
        background-color: white;
        padding: 2rem;
        border-radius: 1rem;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
        overflow: auto;
        box-sizing: border-box;
    }

    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 2rem;
    }

    .left-group {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .header h1 {
        font-size: 2.2rem;
        font-weight: 600;
        color: var(--text);
    }

    .create-btn {
        background-color: var(--primary);
        border: none;
        padding: 0.5rem 1rem;
        border-radius: 12px;
        font-weight: bold;
        font-size: 1rem;
        cursor: pointer;
        color: white;
        transition: background-color 0.2s ease;
    }

    .create-btn:hover {
        background-color: var(--primary-hover);
    }

    .username {
        font-size: 1.5rem;
        color: var(--muted);
        text-shadow: #ffc0cb;
        margin-right: 1rem;
    }

    .whiteboard-list {
        display: flex;
        flex-direction: column;
        border-top: 1px solid #f0f0f0;
    }

    .whiteboard-entry {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        border-bottom: 1px solid #f0f0f0;
        padding: 1rem 0;
        font-size: 1rem;
        flex-wrap: wrap;
        text-decoration: none;
        color: var(--text);
        transition: background-color 0.2s ease;
    }

    .whiteboard-entry:hover {
        background-color: #ffe6ec;
        cursor: pointer;
    }

    .info {
        display: flex;
        flex-direction: column;
        gap: 0.25rem;
        padding-left: 1rem;
    }

    .title {
        font-weight: 600;
        font-size: 1.1rem;
    }

    .author {
        color: #888;
        font-size: 0.95rem;
        font-weight: 400;
    }

    .updated {
        font-size: 0.85rem;
        color: var(--muted);
        padding-right: 1rem;
    }

    button {
        font-size: 1.5rem;
        background: none;
        border: none;
        color: var(--primary);
        font-weight: bold;
        cursor: pointer;
        transition: color 0.2s ease;
    }

    button:hover {
        color: var(--primary-hover);
    }

    
</style>
