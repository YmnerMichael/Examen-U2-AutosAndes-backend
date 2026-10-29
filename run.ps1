$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
if (-not $env:JAVA_HOME) {
  $jdk = Join-Path $env:USERPROFILE '.jdks/ms-21.0.11'
  if (Test-Path $jdk) { $env:JAVA_HOME = $jdk }
}
$maven = Get-Command mvn -ErrorAction SilentlyContinue
if ($maven) { & $maven.Source spring-boot:run; exit $LASTEXITCODE }
$wrapper = Get-ChildItem (Join-Path $env:USERPROFILE '.m2/wrapper/dists') -Filter mvn.cmd -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $wrapper) { throw 'Instala Maven 3.9+ o configura mvn en PATH.' }
& $wrapper.FullName spring-boot:run
exit $LASTEXITCODE
