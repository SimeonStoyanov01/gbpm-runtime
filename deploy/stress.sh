#!/bin/bash

# 1. Create the payload file
cat << 'EOF' > payload.json
{
  "caseId": "FUSLL-001",
  "orderId": "ORD-2041",
  "workObject": {
    "objectId": "OBJ-001",
    "type": "car",
    "material": "steel"
  },
  "designApproved": true,
  "qualityCheckPassed": true,
  "finalInspectionPassed": true
}
EOF

URL="http://localhost:8080/api/process-definitions/2251799813698655/instances"

echo "🚀 Blasting Spring app with ~100 Requests Per Second..."

# 2. Run for 10 seconds
for sec in {1..5}; do
  echo "Sending batch $sec..."
  
  # Fire 100 background curl processes instantly
  for req in {1..100}; do
    curl -s -o /dev/null -w "%{http_code}\n" \
         -X POST \
         -H "Content-Type: application/json" \
         -d @payload.json \
         "$URL" &
  done
  
  # Pause for exactly 1 second before the next wave
  sleep 1
done

# Wait for all background curls to finish
wait
rm payload.json
echo "🏁 Attack finished."
