import { exec, nodeProjects } from "./util.js"

for (let { dir } of nodeProjects) {
  await exec("pnpm update", dir)
}
