import { exec, nodeProjects } from "./util.js"
import * as fs from "node:fs"
import path from "node:path"
import chalk from "chalk"

const fixReg = /^fix(\(.+\))?:/
const featReg = /^feat(\(.+\))?:/
const versionReg = /"version":(.+),/

const sinceTime = fs.readFileSync("./commit.log", "utf-8").trim()

for (let { dir, dirname } of nodeProjects) {
  let logs = await exec('git log --oneline --pretty=format:"%s" --since="' + sinceTime + '"', dir)
  logs = logs
    .map((str) => str.split("\n"))
    .flat()
    .map((str) => str.trim())
  let feat = 0,
    fix = 0
  for (let log of logs) {
    if (featReg.test(log)) feat++
    else if (fixReg.test(log)) fix++
  }
  let packageJson = fs.readFileSync(path.join(dir, "package.json"), "utf-8")
  const matchResult = packageJson.match(versionReg)
  if (!matchResult) throw new Error("not match version:" + dir)
  const field = matchResult[0]
  let version = matchResult[1].trim()
  version = version.slice(1, version.length - 1)
  const versionNumbers = version.split(".").map(Number)
  if (feat) {
    versionNumbers[1] += feat
    versionNumbers[2] = fix
  } else if (fix) {
    versionNumbers[2] += fix
  } else continue
  version = versionNumbers.join(".")
  packageJson = packageJson.replace(field, `"version": "${version}",`)
  fs.writeFileSync(path.join(dir, "package.json"), packageJson)
  console.log(chalk.blue(dirname) + ":", chalk.red(version))
  await exec("git add .", dir)
  await exec(`git commit -m "chore: upgrade package version"`, dir)
}

fs.writeFileSync("./commit.log", new Date().toISOString())
