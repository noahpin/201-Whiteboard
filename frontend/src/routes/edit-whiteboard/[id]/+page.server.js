/***
 * in here wed load the file data from the backend, using the id in the url params 
 */

export function load({ params }) {
	return {
		id: params.id
	};
}