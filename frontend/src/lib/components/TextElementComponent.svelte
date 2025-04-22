<script>
	import { onMount, onDestroy } from 'svelte';
	import { Editor } from '@tiptap/core';
	import StarterKit from '@tiptap/starter-kit';
	import GrabbableElement from "./GrabbableElement.svelte";
	let {
		panX = 0,
		panY = 0,
		elementData = $bindable(null),
		requestSave = void 0,
		startEdit = void 0,
		endEdit = void 0,
		currentTool,
		requestEraseElement = void 0,
	} = $props();
	let editor;
	let editing = false;
	let textEditor;
	let timeout;
	let firstTransaction = true;
	onMount(() => {
		editor = new Editor({
			element: textEditor,
			extensions: [StarterKit],
			content: elementData.content,
			onTransaction: () => {
				editor = editor;
				if(firstTransaction) {
					firstTransaction = false;
					return;
				}
				elementData.updateTextContent(editor.getHTML());
				if (timeout) {
					clearTimeout(timeout);
				}
				timeout = setTimeout(() => {
					endEdit();
					requestSave();
					editing = false;
				}, 1000);
				if (!editing) {
					editing = true;
					startEdit();
				}
				if (firstTransaction) {
					firstTransaction = false;
				}
			},
		});
	});
    let erase = $state(false);

	onDestroy(() => {
		if (editor) {
			editor.destroy();
		}
	});
</script>

<svelte:body onpointerup={()=>{
    if(erase) {
        erase = false;
        requestEraseElement(elementData);
    }
}}/>

<GrabbableElement {currentTool} {panX} {panY}
{startEdit}
{endEdit} bind:elementData {requestSave}>
	<div class="editor" bind:this={textEditor}
	class:erase={erase}
    onmouseover={(e) => {
        if(currentTool === "erase" && e.buttons === 1) {
            erase = true;
        }
    }}
	></div>
</GrabbableElement>
