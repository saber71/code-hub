import * as child_process from "node:child_process"
import chalk from "chalk"
import * as fs from "node:fs"
import * as path from "node:path"

export const nodeProjects = []

const __dirname = path.resolve(".")
const dirs = fs.readdirSync(__dirname)
for (let dir of dirs) {
  const dirname = dir
  dir = path.join(__dirname, dir)
  if (fs.existsSync(path.join(dir, "package.json"))) {
    nodeProjects.push({
      dirname,
      dir
    })
  }
}

export function exec(cmd, cwd) {
  console.log(chalk.yellow(path.basename(cwd)) + ":", cmd)
  const childProcess = child_process.exec(cmd, { cwd }, (error, _, stderr) => {
    if (error) throw error
    if (stderr) console.error(...stderr.split("\n").map((str) => chalk.red(str) + "::" + str + "\n"))
  })
  childProcess.on("exit", (code) => {
    console.log(chalk.blue(path.basename(cwd)) + ":", chalk.yellowBright(cmd), "finished with code", code)
  })
}
