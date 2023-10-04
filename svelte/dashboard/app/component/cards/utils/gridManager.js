export class GridManager {
  /**
   *
   * @type {array<Grid>}
   */
  static grids = [];

  /**
   *
   * @param grid {Grid}
   */
  static registerGrid(grid) {
    this.grids.push(grid);
  }

  /**
   * Removes a Grid from the grids array by object reference.
   *
   * @param {Grid} grid The Grid object to be removed.
   */
  static removeGrid(grid) {
    const index = GridManager.grids.indexOf(grid);
    if (index !== -1) {
      GridManager.grids.splice(index, 1);
    }
  }

  /**
   * Removes a Grid from the grids array by its ID.
   *
   * @param {Grid} grid Id of the grid object to be removed.
   */
  static removeGridById(grid) {
    const index = GridManager.grids.findIndex((g) => g.id === grid.id);
    if (index !== -1) {
      GridManager.grids.splice(index, 1);
    }
  }

  /**
   * @return {Grid}
   */
  static getGridById() {}
}
