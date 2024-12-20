import { Item } from "./item";

/**
 * GridResolver resolves xy grid -
 * resolver checks if items are not overflowing the grid
 * or they are not overlapping each other items have x,y coordinate and w,h as their span
 */
export class GridResolver {
  /**
   *
   *  Method tries to place new item to prearraranged gridItem list
   * @param x {number}
   * @param y {number}
   * @param gridItems {array<Item>}
   * @param item {Item}
   */
  static resolve(x, y, gridItems, item) {
    if (this.isPossible(x, y, [...gridItems, item])) {
      console.log("Everything seems fine ");
      return gridItems;
    } else {
      let possibleItem = { ...item };
      let possible = false;
      for (let i = 0; i < y; i++) {
        for (let j = 0; j < x; j++) {
          possibleItem.x = j;
          possibleItem.y = i;

          if (this.isPossible(x, y, [...gridItems, possibleItem])) {
            possible = true;
            break;
          }
        }
        if (possible) break;
      }

      if (!possible) {
        return gridItems;
      } else {
        item.x = possibleItem.x;
        item.y = possibleItem.y;
        return [...gridItems, item];
      }
    }
  }

  /**
   *  Method tries to reaarange gridItems around the new item
   * @param x {number}
   * @param y {number}
   * @param gridItems {array<Item>}
   * @param item {Item}
   */
  static resolveAroundItem(x, y, gridItems, item) {
    if (this.isPossible(x, y, [item, ...gridItems])) {
      return gridItems;
    } else {
      let overlapped = this.isPossible(x, y, [item, ...gridItems], true);

      let overlappedCopy = { ...overlapped, _x: 0, _y: 0 };

      for (let i = 0; i <= y - overlapped._h; i++) {
        for (let j = 0; j <= x - overlapped._w; j++) {
          overlappedCopy._x = j;
          overlappedCopy._y = i;

          if (
            this.isPossible(x, y, [
              item,
              ...gridItems.filter((it) => it !== overlapped),
              overlappedCopy,
            ])
          ) {
            overlapped.x = j;
            overlapped.y = i;

            return [item, ...gridItems];
          }
        }
      }

      return gridItems; // If all else fails, return the original gridItems
    }
  }

  /**
   *
   *  Method checks if current organization of items is possible
   * @param x {number} columns of the grid
   * @param y {number} rows of the grid
   * @param gridItems {array<Item>}
   * @param returnItem if false function also returns the item that overlaps, it is always the latter one in the array
   * @param gridItems.Item.x coordinate
   * @param gridItems.Item.y coordinate
   * @param gridItems.Item.w width span
   * @param gridItems.Item.h height span
   * @return {boolean|Item}
   * */
  static isPossible(x, y, gridItems, returnItem = false) {
    // Create a blank grid
    const grid = Array.from({ length: y }).map(() => Array(x).fill(null));

    for (const item of gridItems) {
      // for the height of the item
      for (let i = 0; i < item._h; i++) {
        // for the width of the item
        for (let j = 0; j < item._w; j++) {
          // Check if the item's placement exceeds grid boundaries
          if (item._y + i >= y || item._x + j >= x) {
            if (returnItem) {
              return item;
            }
            return false;
          }
          // Check if the item's placement overlaps with another item
          if (grid[item._y + i][item._x + j] !== null) {
            if (returnItem) {
              return item;
            }
            return false;
          }
          // Place the item on the grid
          grid[item._y + i][item._x + j] = item;
        }
      }
    }
    return true;
  }

  /**
   * @param newItem
   * @param gridItems
   * @param w
   * @param h
   */
  static findSuitablePosition(newItem, gridItems, w, h) {
    if (gridItems.length > 0) {
      console.log(gridItems);
      // Convert gridItems to a 2D array
      const grid = this.gridAsArray(w, h, gridItems);

      // Iterate through the grid to find a suitable position
      for (let row = 0; row <= h - newItem.h; row++) {
        for (let col = 0; col <= w - newItem.w; col++) {
          let canPlace = true;

          // Check if the space is empty in the grid
          for (let i = row; i < row + newItem.h; i++) {
            for (let j = col; j < col + newItem.w; j++) {
              if (grid[i][j] !== " ") {
                canPlace = false;
                break;
              }
            }
            if (!canPlace) {
              break;
            }
          }

          // If a suitable position is found, return the coordinates
          if (canPlace) {
            // console.log("Suitable position found at:", { x: col, y: row });
            return { x: col, y: row };
          }
        }
      }

      // console.log("No suitable position found.");
      return null;
    } else {
      // console.log("No items to cope with");
      return { x: newItem.x, y: newItem.y };
    }
  }

  static gridAsString(x, y, items) {
    // Create a blank grid matrix
    const grid = this.gridAsArray(x, y, items);

    // Construct the visual representation
    const horizontalLine = "+---".repeat(x) + "+";
    let output = horizontalLine + "\n";

    grid.forEach((row) => {
      const rowString = row.map((cell) => `| ${cell} `).join("") + "|";
      output += rowString + "\n";
      output += horizontalLine + "\n";
    });

    return output;
  }

  static gridAsArray(x, y, items) {
    const grid = Array.from({ length: y }).map(() => Array(x).fill(" "));

    // Assign labels for items (A, B, C, ...)
    const labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Place each item on the grid matrix
    items.forEach((item, index) => {
      for (let i = 0; i < item.h; i++) {
        for (let j = 0; j < item.w; j++) {
          grid[item.y + i][item.x + j] = labels[index % labels.length];
        }
      }
    });

    return grid;
  }
}
