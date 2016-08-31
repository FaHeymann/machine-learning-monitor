import ResultsController from './controllers/resultsController';
import CreateAlgorithmController from './controllers/createAlgorithmController';
import CreateAlgorithmEnumController from './controllers/createAlgorithmEnumController';

// eslint-disable-next-line no-undef
angular.module('machine-learning-monitor', ['chart.js', 'ui.bootstrap'])

.component('results', {
  controller: ResultsController,
  templateUrl: '/assets/templates/results.html',
  bindings: {
    featureSets: '<',
    algorithms: '<',
  },
})

.component('createAlgorithm', {
  controller: CreateAlgorithmController,
  templateUrl: '/assets/templates/createAlgorithm.html',
})

.component('createAlgorithmEnum', {
  controller: CreateAlgorithmEnumController,
  templateUrl: '/assets/templates/createAlgorithmEnum.html',
  bindings: {
    enumValues: '<',
  },
});
