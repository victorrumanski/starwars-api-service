#!/bin/bash
set -e

echo "🚀 Deploying Star Wars API Facade..."
docker build -t starwars-api-service:latest ./starwars-api-service/
k3d image import starwars-api-service:latest -c learnk8s
kubectl apply -f starwars-api-service/k8s.yaml
kubectl rollout restart deployment starwars-api-service -n apps
kubectl rollout status deployment starwars-api-service -n apps

echo "✅ Star Wars Facade updated!"
