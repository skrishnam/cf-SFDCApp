var Router = require("Router");
var env = require('env');
var jsonmask = require('json-mask');

// Global Variable
var appRouter = new Router();

var sfdcClient = require('http')({
	connectTimeout : 1000,
	socketTimeout : 5000,
});

//get oauth2 token
appRouter.get('/oauth2', function(req, res) {
	var url = req.url+"";
	var baseurl = url.substr(0,url.indexOf("/api"));
	var reqObj = {url: baseurl+"/oauth2", method: "GET", body: ""};
	getSFDCObj(reqObj, req, res);
});

// get all contacts by accounts
appRouter.get('/accounts', function(req, res) {
	var url = req.url+"";
	var baseurl = url.substr(0,url.indexOf("/api"));
//	console.log("baseurl: "+baseurl);
	var reqObj = {url: baseurl+"/accounts", method: "GET", body: ""};
	getSFDCObj(reqObj, req, res);
});

//get all opportunities by accounts
appRouter.get('/opp_by_accts', function(req, res) {
	var url = req.url+"";
	var baseurl = url.substr(0,url.indexOf("/api"));
	var reqObj = {url: baseurl+"/opp_by_accts", method: "GET", body: ""};
	getSFDCObj(reqObj, req, res);
});

// create/retrieve/update/delete account detail by id
appRouter.all('/account/:id', function(req, res, id) {
	//console.log("req.method "+ req.method);
	//console.log("req.body "+ req.body);
	var url = req.url+"";
	var baseurl = url.substr(0,url.indexOf("/api"));
	var reqObj = "";
	switch(req.method) {
		case "POST" : reqObj = {url: baseurl+"/account/new", method: req.method, body: req.body};break;
		case "DELETE" : reqObj = {url: baseurl+"/account/" + id, method: req.method, body: ""};break;
		case "PUT" : reqObj = {url: baseurl+"/account/" + id, method: req.method, body: req.body};break;
		default : reqObj = {url: baseurl+"/account/" + id, method: "GET", body: ""};break;
	}
	getSFDCObj(reqObj, req, res);
});

// create/retrieve/update/delete contact detail by id
appRouter.all('/contact/:id', function(req, res, id) {
	var url = req.url+"";
	var baseurl = url.substr(0,url.indexOf("/api"));
	var reqObj = "";
	switch(req.method) {
		case "POST" : reqObj = {url: baseurl+"/contact/new", method: req.method, body: req.body};break;
		case "DELETE" : reqObj = {url: baseurl+"/contact/" + id, method: req.method, body: ""};break;
		case "PUT" : reqObj = {url: baseurl+"/contact/" + id, method: req.method, body: req.body};break;
		default : reqObj = {url: baseurl+"/contact/" + id, method: "GET", body: ""};break;
	}
	getSFDCObj(reqObj, req, res);
});

// create/retrieve/update/delete opportunity by id
appRouter.all('/opportunity/:id', function(req, res, id) {
	var url = req.url+"";
	var baseurl = url.substr(0,url.indexOf("/api"));
	var reqObj = "";
	switch(req.method) {
		case "POST" : reqObj = {url: baseurl+"/opportunity/new", method: req.method, body: req.body};break;
		case "DELETE" : reqObj = {url: baseurl+"/opportunity/" + id, method: req.method, body: ""};break;
		case "PUT" : reqObj = {url: baseurl+"/opportunity/" + id, method: req.method, body: req.body};break;
		default : reqObj = {url: baseurl+"/opportunity/" + id, method: "GET", body: ""};break;
	}
	getSFDCObj(reqObj, req, res);
});

appRouter
		.all(
				'/*catchall',
				function(req, res) {
					var url = req.url+"";
					var baseurl = url.substr(0,url.indexOf("/api"));
					res
							.setBody({
								note : 'API Gateway to provide SalesForce.com\'s business objects.',
								links : [
										{
											title : 'List of Accounts with main Contact info',
											href : baseurl + '/api/accounts'
										},
										{
											title : 'Account object by id',
											href : baseurl
													+ '/api/account/account_id'
										},
										{
											title : 'Contact object by id',
											href : baseurl
													+ '/api/contact/contact_id'
										},
										{
											title : 'List of Accounts with all of the associated Opportunity info',
											href : baseurl + '/api/opp_by_accts'
										},
										{
											title : 'Opportunity object by id',
											href : baseurl
													+ '/api/opportunity/opportunity_id'
										} ]
							});
				});

function getSFDCObj(reqObj, req, res) {
	console.log('headers: ' + req.headers.token);
	res.setBody(getResult(reqObj, req));
}

function getResult(reqObj, req) {
	result = sfdcClient.request({
		url : reqObj.url,
		headers : {
			Accept : 'application/json',
		},
		method : reqObj.method,
		body : reqObj.body
	}, function(response) {
		// the callback function will execute on
		// the response when its ready
		console.log(response.statusCode);
		console.log(response.body);
		var myJSONObject = (req.method == "GET" || req.method == "POST") ? eval('(' + response.body + ')') : {status: "completed"};
		console.log(jsonmask(myJSONObject,req.parameters.fields));
		return {
			status : response.statusCode,
			data : (req.parameters.fields) ? jsonmask(myJSONObject,req.parameters.fields) : myJSONObject
		};
	});
	return result;
}

module.exports = appRouter;
