$(function($) {

	/*
	 * Absolute path instead of js/Ajax.. from the root of the context, to be able to be used for:
	 * - search.jsp (context in /Datafari)
	 * - and login.jsp (context in /Datafari/admin)
	 */ 
	window.i18n.setLanguageUrl('/Datafari/js/AjaxFranceLabs/locale/');
	
	window.i18n.setupLanguage('Datafari home page');

	Manager = new AjaxFranceLabs.Manager({
		serverUrl : window.location.protocol + "//" + window.location.host
				+ '/Datafari/SearchProxy/',
		constellio : false,
		connectionInfo : {
			autocomplete : {
				serverUrl : '',
				servlet : 'suggest',
				queryString : 'q='
			},
			spellcheck : {
				serverUrl : '',
				servlet : '',
				queryString : ''
			}
		}
	});

	Manager.store.addByValue("fl", 'title,url,id,extension');

});
