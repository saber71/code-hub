import { exec, nodeProjects } from "./util.js"

for (let { dir } of nodeProjects) {
  exec("pnpm update", dir)
}
