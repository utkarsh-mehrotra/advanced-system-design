import os
import glob
import subprocess
import inquirer
from rich.console import Console
from rich.panel import Panel

console = Console()

SRC_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "src")

def get_base_domains():
    bases = set()
    for d in os.listdir(SRC_DIR):
        if not os.path.isdir(os.path.join(SRC_DIR, d)): continue
        
        b = d
        if d.endswith("_sde2"): b = d[:-5]
        elif d.endswith("_sde3"): b = d[:-5]
        elif d.endswith("_upgraded"): b = d[:-9]
        
        bases.add(b)
    return sorted(list(bases))

def get_available_tiers(domain):
    tiers = {}
    base_folder = os.path.join(SRC_DIR, domain)
    sde2_folder = os.path.join(SRC_DIR, domain + "_sde2")
    sde3_folder = os.path.join(SRC_DIR, domain + "_sde3")
    upgraded_folder = os.path.join(SRC_DIR, domain + "_upgraded")

    if os.path.exists(base_folder): tiers["SDE1 (Base)"] = domain
    if os.path.exists(sde2_folder): tiers["SDE2 (Thread-Safe)"] = domain + "_sde2"
    if os.path.exists(sde3_folder): tiers["SDE3 (Lock-Free / Event-Driven)"] = domain + "_sde3"
    if os.path.exists(upgraded_folder): tiers["SDE3 (Upgraded)"] = domain + "_upgraded"
    
    return tiers

def main():
    console.print(Panel.fit("[bold cyan]Welcome to the System Design Executable Runner[/bold cyan]\n[green]Master LLD Demo Repository -> SDE1 to SDE3[/green]"))

    domains = get_base_domains()
    if not domains:
        console.print("[red]No systems found in src/ directory.[/red]")
        return

    domain_question = [
        inquirer.List('domain', message="Select the System you want to run", choices=domains, carousel=True)
    ]
    domain_answer = inquirer.prompt(domain_question)
    if not domain_answer: return
    selected_domain = domain_answer['domain']

    tiers = get_available_tiers(selected_domain)
    tier_question = [
        inquirer.List('tier', message="Select the Architecture Tier", choices=list(tiers.keys()), carousel=True)
    ]
    tier_answer = inquirer.prompt(tier_question)
    if not tier_answer: return
    
    selected_tier_name = tier_answer['tier']
    target_package = tiers[selected_tier_name]
    target_dir = os.path.join(SRC_DIR, target_package)
    
    # Find matching Demo files roughly via "Demo" or Main
    java_files = glob.glob(os.path.join(target_dir, "*.java"))
    demo_files = [f for f in java_files if "Demo" in os.path.basename(f) or "Main" in os.path.basename(f)]
    
    # If no obvious demo, look for files containing 'public static void main'
    if not demo_files:
        for f in java_files:
            with open(f, 'r') as file:
                if "public static void main" in file.read():
                    demo_files.append(f)
                    
    if not demo_files:
        console.print("[red]No main/demo entrypoint found in this package![/red]")
        return
        
    demo_file = demo_files[0]
    if len(demo_files) > 1:
        file_choices = [os.path.basename(f) for f in demo_files]
        file_q = [inquirer.List('file', message="Multiple Demos found. Select one:", choices=file_choices)]
        ans = inquirer.prompt(file_q)
        demo_file = os.path.join(target_dir, ans['file'])
        
    demo_class = os.path.basename(demo_file)[:-5] # remove .java
    
    console.print(f"\n[bold yellow]Compiling[/bold yellow] entire workspace using Maven...")
    
    # Compile
    cmd = f"mvn clean compile -q"
    process = subprocess.run(cmd, shell=True, cwd=os.path.dirname(SRC_DIR))
    if process.returncode != 0:
        console.print("[red]Compilation failed. Please check logs.[/red]")
        return
        
    console.print(f"[bold green]Compilation successful![/bold green]")
    console.print(f"[bold cyan]Executing[/bold cyan] -> {target_package}.{demo_class}\n")
    console.print("-" * 50)
    
    # Execution using exec:java
    run_cmd = f"mvn exec:java -Dexec.mainClass=\"{target_package}.{demo_class}\" -q"
    subprocess.run(run_cmd, shell=True, cwd=os.path.dirname(SRC_DIR))
    
    console.print("-" * 50)
    console.print(f"\n[green]Execution finished.[/green]")

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        console.print("\nExiting...")
