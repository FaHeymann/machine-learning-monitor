import ResultsController from './controllers/resultsController';
import CreateAlgorithmController from './controllers/createAlgorithmController';
import CreateAlgorithmEnumController from './controllers/createAlgorithmEnumController';

// eslint-disable-next-line no-undef
angular.module('machine-learning-monitor', ['chart.js', 'ui.bootstrap'])

.directive('results', () => {
  return {
    restrict: 'A',
    scope: true,
    controllerAs: 'ctrl',
    bindToController: {
      featureSetId: '@',
      algorithmId: '@',
    },
    controller: ResultsController,
  };
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
