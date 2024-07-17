import { exec, nodeProjects } from "./util.js"

for (let { dir, pip } of nodeProjects) {
  if (pip) exec("pipenv run pipenv sync", dir)
  exec("pnpm install", dir)
}
