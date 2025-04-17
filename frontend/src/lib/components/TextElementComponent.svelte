<script>
	import { onMount, onDestroy } from 'svelte';
	import { Editor } from '@tiptap/core';
	import StarterKit from '@tiptap/starter-kit';
	import GrabbableElement from "./GrabbableElement.svelte";
	let {
		panX = 0,
		panY = 0,
		elementData = $bindable(null),
	} = $props();
	let editor;
	let textEditor;
	onMount(() => {
		editor = new Editor({
			element: textEditor,
			extensions: [StarterKit],
			content: '<p>Hello World! 🌍️ </p>',
			onTransaction: () => {
				editor = editor;
			},
		});
	});

	onDestroy(() => {
		if (editor) {
			editor.destroy();
		}
	});
</script>

<GrabbableElement {panX} {panY} bind:elementData>
	<div class="editor" bind:this={textEditor}></div>
</GrabbableElement>
