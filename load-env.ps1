# PowerShell script to load environment variables from .env file
# Usage: .\load-env.ps1

$envFile = Join-Path $PSScriptRoot ".env"

if (-Not (Test-Path $envFile)) {
    Write-Host "Error: .env file not found at $envFile" -ForegroundColor Red
    Write-Host "Please copy .env.example to .env and configure your settings" -ForegroundColor Yellow
    exit 1
}

Write-Host "Loading environment variables from .env file..." -ForegroundColor Green

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    
    # Skip empty lines and comments
    if ($line -eq "" -or $line.StartsWith("#")) {
        return
    }
    
    # Parse KEY=VALUE
    if ($line -match '^([^=]+)=(.*)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        
        # Remove quotes if present
        $value = $value -replace '^["'']|["'']$', ''
        
        # Set environment variable for current process
        [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
        
        Write-Host "  $name = $value" -ForegroundColor Cyan
    }
}

Write-Host "`nEnvironment variables loaded successfully!" -ForegroundColor Green
Write-Host "You can now run: ./gradlew run" -ForegroundColor Yellow
