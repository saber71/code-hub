import { exec, nodeProjects } from "./util.js"

for (let { dir } of nodeProjects) {
  exec("pnpm run build", dir)
}
