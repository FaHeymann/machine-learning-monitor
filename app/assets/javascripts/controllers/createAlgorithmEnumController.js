export default class CreateAlgorithmEnumController {
  addValue() {
    this.enumValues.push({
      name: '',
    });
  }

  removeValue(index) {
    if (index > -1) {
      this.enumValues.splice(index, 1);
    }
  }
}
