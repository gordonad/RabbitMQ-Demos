//   The contents of this file are subject to the Mozilla Public License
//   Version 1.1 (the "License"); you may not use this file except in
//   compliance with the License. You may obtain a copy of the License at
//   http://www.mozilla.org/MPL/
//
//   Software distributed under the License is distributed on an "AS IS"
//   basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//   License for the specific language governing rights and limitations
//   under the License.
//
//   The Original Code is RabbitMQ.
//
//   The Initial Developers of the Original Code are LShift Ltd,
//   Cohesive Financial Technologies LLC, and Rabbit Technologies Ltd.
//
//   Portions created before 22-Nov-2008 00:00:00 GMT by LShift Ltd,
//   Cohesive Financial Technologies LLC, or Rabbit Technologies Ltd
//   are Copyright (C) 2007-2008 LShift Ltd, Cohesive Financial
//   Technologies LLC, and Rabbit Technologies Ltd.
//
//   Portions created by LShift Ltd are Copyright (C) 2007-2009 LShift
//   Ltd. Portions created by Cohesive Financial Technologies LLC are
//   Copyright (C) 2007-2009 Cohesive Financial Technologies
//   LLC. Portions created by Rabbit Technologies Ltd are Copyright
//   (C) 2007-2009 Rabbit Technologies Ltd.
//
//   All Rights Reserved.
//
//   Contributor(s): ______________________________________.
//
//

var JsonRpc;
var RabbitMQ;
(function () {
     var Support = {
	 extend: Object.extend,
	 bindEvent: function (elt, event, cb) {
	     Event.observe(elt, event, cb);
	 },
	 each: function (collection, callback) {
	     return collection.each(callback);
	 },
	 ajaxPost: function (url, customHeaders, bodyString, callback) {
	     var headers = ['Content-type', 'application/json',
			    'Accept', 'application/json'];
	     for (var header in customHeaders) {
		 if (customHeaders.hasOwnProperty(header)) {
		     headers.push(header, customHeaders[header]);
		 }
	     }
	     return new Ajax.Request(url, { method: 'post',
					    requestHeaders: headers,
					    postBody: bodyString,
					    onComplete: callback });
	 }
     };

     JsonRpc = JsonRpc_ModuleFactory(Support);
     RabbitMQ = RabbitMQ_ModuleFactory(JsonRpc, Support);
 })();
