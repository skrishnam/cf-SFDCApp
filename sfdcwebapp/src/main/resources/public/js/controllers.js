/*
 * JS file for all of the Angular controllers in the app
 */
'use strict';

/*
 * Define the SFDC controllers scope for Angular
 */
var sfdcControllers = angular.module('sfdcControllers', []);

sfdcApp.controller('AccountListController', function($scope, $http) {
	
	$scope.getAccounts = function() {
		// when landing on the page, get all accounts and show them
		$http.get('/accounts').success(function(data) {
			console.log("response data: " + JSON.stringify(data));
			$scope.accounts = data;
		}).error(function(data) {
			console.log('Error: ' + data);
			$scope.message = data.message;
			$scope.error = data.code;
		});
	};
	
	//Handles the delete request function
	$scope.delete = function(accountId) {
		//console.log("deleting id " + accountId);
		$http.delete('/account/'+accountId).success(function(data) {
			$scope.getAccounts();
		}).error(function(data) {
			console.log('Error: ' + data);
			$scope.message = data.message;
			$scope.error = data.code;
		});
	};

	// Initial page load
	$scope.getAccounts();
});

sfdcApp.controller('EditAccountInfoController', function($scope, $http, $routeParams) {
	//Handles the update request function
	$scope.update = function(account) {
		//console.log("updating account: "+JSON.stringify(account));
		if($routeParams.id == "new") {
			$http.post("/account/new", account).success(function(data, status, headers, config) {
				$http.get("/account/" + data.id).success(function(data) {
					$scope.account = data;
					$scope.message = "Successfully saved the account.";
					$scope.error = "";
				});				
			}).error(function(data, status, headers, config) {
				$scope.message = "";
				$scope.error = "There was an error saving the account.";
			});
		} else {
			$http.put("/account/" + $routeParams.id, account).success(function(data, status, headers, config) {
				$http.get("/account/" + $routeParams.id).success(function(data) {
					$scope.account = data;
					$scope.message = "Successfully saved the account.";
					$scope.error = "";
				});				
			}).error(function(data, status, headers, config) {
				$scope.message = "";
				$scope.error = "There was an error saving the account.";
			});			
		}
	};
	
	//Handles the delete request function of the account
	$scope.delete = function(id) {
		if($routeParams.id != "new") {
			$http.delete('/account/'+id).success(function(data) {
				$scope.account = "";
				$scope.message = "Successfully deleted the account.";
				$scope.error = "";
			});
		}
	}

	//Handles the reset request function and the initial load of the entry
	$scope.reset = function() {
		if($routeParams.id != "new") {
			$http.get("/account/" + $routeParams.id).success(function(data) {
				$scope.account = data;
			});
		}
	}
	
	//Initial load
	$scope.reset();
});

sfdcApp.controller('OppAcctListController', function($scope, $http) {
	$scope.getAccounts = function() {
		$http.get('/opp_by_accts').success(function(data) {
			console.log("response data: " + JSON.stringify(data));
			$scope.accounts = data;
		}).error(function(data) {
			console.log('Error: ' + data);
			$scope.message = data.message;
			$scope.error = data.code;
		});
	};

	// Initial page load
	$scope.getAccounts();
});

sfdcApp.controller('AccountInfoController', function($scope, $http, $routeParams) {
	$scope.getAccount = function() {
		$http.get("/account/" + $routeParams.id).success(function(data) {
			$scope.account = data;
		});				
	};

	//Handles the delete request function of the account
	$scope.delete = function(id) {
		if($routeParams.id != "new") {
			$http.delete('/account/'+id).success(function(data) {
				$scope.account = "";
				$scope.message = "Successfully deleted the account.";
				$scope.error = "";
			});
		}
	}

	
	// Initial page load
	$scope.getAccount();
});

sfdcApp.controller('EditContactInfoController', function($scope, $http, $routeParams) {
	//Handles the update request function
	$scope.update = function(contact) {
		if($routeParams.id == "new") {
			contact.AccountId = $routeParams.accountId;
			$http.post("/contact/new", contact).success(function(data, status, headers, config) {
				$http.get("/contact/" + data.id).success(function(data) {
					$scope.contact = data;
					$scope.message = "Successfully saved the contact.";
					$scope.error = "";
				});				
			}).error(function(data, status, headers, config) {
				$scope.message = "";
				$scope.error = "There was an error saving the contact.";
			});
		} else {
			$http.put("/contact/" + $routeParams.id, contact).success(function(data, status, headers, config) {
				$http.get("/contact/" + $routeParams.id).success(function(data) {
					$scope.contact = data;
					$scope.message = "Successfully saved the contact.";
					$scope.error = "";
				});				
			}).error(function(data, status, headers, config) {
				$scope.message = "";
				$scope.error = "There was an error saving the contact.";
			});			
		}
	};
	
	//Handles the delete request function and the initial load of the entry
	$scope.delete = function(id) {
		if($routeParams.id != "new") {
			$http.delete("/contact/" + id).success(function(data) {
				$scope.contact = "";
				$scope.message = "Successfully deleted the contact.";
				$scope.error = "";
			});
		}
	}

	//Handles the reset request function and the initial load of the entry
	$scope.reset = function() {
		if($routeParams.id != "new") {
			$http.get("/contact/" + $routeParams.id).success(function(data) {
				$scope.contact = data;
			});
		}
	}
	
	//Initial load
	$scope.reset();
});

sfdcApp.controller('ContactInfoController', function($scope, $http, $routeParams) {
	$scope.getContact = function() {
		$http.get('/contact/'+$routeParams.id).success(function(data) {
			//console.log("response data: " + JSON.stringify(data));
			$scope.contact = data;
		}).error(function(data) {
			console.log('Error: ' + data);
			$scope.message = data.message;
			$scope.error = data.code;
		});
	};

	//Handles the delete request function and the initial load of the entry
	$scope.delete = function(id) {
		if($routeParams.id != "new") {
			$http.delete("/contact/" + id).success(function(data) {
				$scope.contact = "";
				$scope.message = "Successfully deleted the contact.";
				$scope.error = "";
			});
		}
	}

	// Initial page load
	$scope.getContact();
});

sfdcApp.controller('EditOpportunityInfoController', function($scope, $http, $routeParams) {
	//Handles the update request function
	$scope.update = function(opportunity) {
		if($routeParams.id == "new") {
			opportunity.AccountId = $routeParams.accountId;
			$http.post("/opportunity/new", opportunity).success(function(data, status, headers, config) {
				$http.get("/opportunity/" + data.id).success(function(data) {
					$scope.opportunity = data;
					$scope.message = "Successfully saved the opportunity.";
					$scope.error = "";
				});				
			}).error(function(data, status, headers, config) {
				$scope.message = "";
				$scope.error = "There was an error saving the opportunity.";
			});
		} else {
			$http.put("/opportunity/" + $routeParams.id, opportunity).success(function(data, status, headers, config) {
				$http.get("/opportunity/" + $routeParams.id).success(function(data) {
					$scope.opportunity = data;
					$scope.message = "Successfully saved the opportunity.";
					$scope.error = "";
				});				
			}).error(function(data, status, headers, config) {
				$scope.message = "";
				$scope.error = "There was an error saving the opportunity.";
			});			
		}
	};
	
	//Handles the delete request function and the initial load of the opportunity
	$scope.delete = function(id) {
		if($routeParams.id != "new") {
			$http.delete("/opportunity/" + id).success(function(data) {
				$scope.opportunity = "";
				$scope.message = "Successfully deleted the opportunity.";
				$scope.error = "";
			});
		}
	}

	//Handles the reset request function and the initial load of the opportunity
	$scope.reset = function() {
		if($routeParams.id != "new") {
			$http.get("/opportunity/" + $routeParams.id).success(function(data) {
				$scope.opportunity = data;
				$scope.opportunity.CloseDate = new Date($scope.opportunity.CloseDate);
			});
		}
	}
	
	//Initial load
	$scope.reset();
});

sfdcApp.controller('OpportunityInfoController', function($scope, $http, $routeParams) {
	$scope.getOpportunity = function() {
		$http.get('/opportunity/'+$routeParams.id).success(function(data) {
			//console.log("response data: " + JSON.stringify(data));
			$scope.opportunity = data;
//			$scope.opportunity.CloseDate = new Date($scope.opportunity.CloseDate);
//			$scope.opportunity.CloseDate.format = "MM/DD/YYYY";
		}).error(function(data) {
			console.log('Error: ' + data);
			$scope.message = data.message;
			$scope.error = data.code;
		});
	};

	//Handles the delete request function and the initial load of the opportunity
	$scope.delete = function(id) {
		if($routeParams.id != "new") {
			$http.delete("/opportunity/" + id).success(function(data) {
				$scope.opportunity = "";
				$scope.message = "Successfully deleted the opportunity.";
				$scope.error = "";
			});
		}
	}
	
	// Initial page load
	$scope.getOpportunity();
});
