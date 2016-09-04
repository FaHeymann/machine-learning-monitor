import ResultsController from './controllers/resultsController';
import CreateAlgorithmController from './controllers/createAlgorithmController';
import CreateAlgorithmEnumController from './controllers/createAlgorithmEnumController';
import RunTestController from './controllers/runTestController';

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

.component('runTest', {
  controller: RunTestController,
  templateUrl: '/assets/templates/runTest.html',
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
})

.directive('formatInt', $filter => {
  return {
    require: '?ngModel',
    link: (scope, elem, attrs, ctrl) => {
      if (!ctrl) {
        return;
      }

      ctrl.$formatters.unshift(() => $filter('number')(ctrl.$modelValue));

      ctrl.$parsers.unshift(viewValue => {
        const plainNumber = viewValue.replace(/[,\.]/g, '');
        const b = $filter('number')(plainNumber);

        elem.val(b);

        return plainNumber;
      });
    },
  };
})

.directive('formatDouble', $filter => {
  return {
    require: '?ngModel',
    link: (scope, elem, attrs, ctrl) => {
      if (!ctrl) {
        return;
      }

      ctrl.$formatters.unshift(() => $filter('number')(ctrl.$modelValue));

      ctrl.$parsers.unshift(viewValue => {
        const trailingDot = viewValue.charAt(viewValue.length - 1) === '.';
        const plainNumber = viewValue.replace(/[,]/g, '');
        let b = '' + $filter('number')(plainNumber);
        b += trailingDot && b.charAt(b.length - 1) !== '.' ? '.' : '';

        elem.val(b);

        return plainNumber;
      });
    },
  };
});
