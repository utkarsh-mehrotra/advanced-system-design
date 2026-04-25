# Pubsubsystem - SDE1 Baseline Implementation

This is the foundational, baseline implementation of the Pubsubsystem system. 

## Architectural Characteristics
- **Structure:** Monolithic design with tightly-coupled domain classes.
- **Concurrency:** Basic `synchronized` locks across broad method scopes, prone to thread bottlenecks.
- **Goal:** Demonstrates a functional Minimum Viable Product (MVP) but lacks scalable decoupling and granular lock optimizations.

*For the highly optimized, enterprise-grade versions, please refer to the `_sde2` and `_sde3` directories.*
