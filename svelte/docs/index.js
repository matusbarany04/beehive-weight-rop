const fs = require('fs');
const path = require('path');
function printFolderTree(startPath, indent = '') {
  const files = fs.readdirSync(startPath);

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    const fullPath = path.join(startPath, file);
    const stats = fs.statSync(fullPath);

    console.log(indent + file);

    if (stats.isDirectory()) {
      printFolderTree(fullPath, indent + '   ');
    }
  }
}

function traverse(sourcePath, destPath, indent = '') {
  const files = fs.readdirSync(sourcePath);

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    const fullSourcePath = path.join(sourcePath, file);
    const fullDestPath = path.join(destPath, file);
    const stats = fs.statSync(fullSourcePath);

    console.log(indent + file);

    if (stats.isDirectory()) {
      if (!fs.existsSync(fullDestPath)) {
        fs.mkdirSync(fullDestPath);
      }
      traverse(fullSourcePath, fullDestPath, indent + '└──');
    } else {
      if (path.extname(file) === '.svelte') {
        let fileContents = fs.readFileSync(fullSourcePath, 'utf8');
        let originalLength = fileContents.length;
        
        // Remove everything after </script>
        fileContents = fileContents.replace('<script>', '');

        // Remove everything after </script> including the </script> tag
        fileContents = fileContents.replace(/<\/script>[\s\S]*$/, '');

        if(originalLength < fileContents.length){
          fs.writeFileSync(fullDestPath + ".js", fileContents, 'utf8');
        }else {
          // there is nothing to document
        }
      } else {
        // Copy non-.svelte files directly
        fs.copyFileSync(fullSourcePath, fullDestPath);
      }
    }
  }
}

function strip(paths){
  const destDir = path.join(__dirname, 'temp');
  fs.rmdirSync(destDir, { recursive: true })
  if (!fs.existsSync(destDir)) {
    fs.mkdirSync(destDir);
  }

  for (let router_path of paths){
    const startPath = path.join(__dirname, '../'+ router_path);
    const destDir = path.join(__dirname, 'temp/' + router_path);
    if (!fs.existsSync(destDir)) {
      fs.mkdirSync(destDir);
    }
    traverse(startPath, destDir);
  }
}

strip(["general", "dashboard", "components"])