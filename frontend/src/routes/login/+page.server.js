/**
 * necessary: svelte action for a login form
 * 
 */



export function load({ params }) {
    console.log(params)
	return {
		redirect: params.redirect
	};
}