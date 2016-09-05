mysql-start:
	/usr/local/opt/mysql/support-files/mysql.server start

lint-java:
	activator checkstyle

lint-js:
	npm run eslint

test: lint-java lint-js
	activator test

start:
	activator run
