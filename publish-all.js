import { nodeProjects, publish } from "./util.js"

for (let { dir, packageJson } of nodeProjects) {
  await publish(dir, packageJson)
}
