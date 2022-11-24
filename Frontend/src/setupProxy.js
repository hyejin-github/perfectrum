const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://j7c105.p.ssafy.io',
      changeOrigin: true,
    })
  );
};

