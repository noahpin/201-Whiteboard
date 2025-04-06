/**
 * this url is bad and does not contain an ID, so we will redirect to the base page
 * 
 */

import { redirect } from '@sveltejs/kit';

export async function load({ url, locals }) {

    throw redirect(303, '/'); 
    return;
}