const http = require('http');
const url = require('url');

const API_BASE_URL = '/api/maths';

function respondToClientSide(resultNumber,res){
  console.log(resultNumber);
  res.writeHead(200, {'Content-Type': 'application/json'});
  res.end( JSON.stringify({res:resultNumber}) );
}

http.createServer(function (req, res) {
  const queryObject = url.parse(req.url,true).query;
  console.log(queryObject);
  console.log(req.url);
  var serviceUrl = req.url.replace((/\?.*/g),'');
  console.log(serviceUrl);

  var p1 = parseInt(queryObject.p1);
  var p2 = parseInt(queryObject.p2);
  switch(serviceUrl){
    case API_BASE_URL+'/add':
      setTimeout(respondToClientSide, 1500, p1+p2,res);
      break;
    case API_BASE_URL+'/multiply':
      setTimeout(respondToClientSide, 2000, p1*p2,res);
      break;
    case API_BASE_URL+'/divide':
      setTimeout(respondToClientSide, 1000, p1/p2,res);
      break;
    default:
      res.writeHead(404, {'Content-Type': 'text/html'});
      res.end('Page not found');
  }
  
}).listen(8080);