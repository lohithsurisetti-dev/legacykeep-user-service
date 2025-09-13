#!/bin/bash

# =============================================================================
# LegacyKeep User Service - Test Script
# =============================================================================

echo "🧪 Testing LegacyKeep User Service"
echo "=================================="

# Configuration
BASE_URL="http://localhost:8082/user"
API_BASE="$BASE_URL/api/v1"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

# Function to run a test
run_test() {
    local test_name="$1"
    local method="$2"
    local url="$3"
    local data="$4"
    local expected_status="$5"
    
    echo -n "Testing $test_name... "
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method \
            -H "Content-Type: application/json" \
            -d "$data" \
            "$url")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$url")
    fi
    
    status_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN}✓ PASSED${NC} (Status: $status_code)"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: $expected_status, Got: $status_code)"
        echo "Response: $body"
        ((TESTS_FAILED++))
    fi
}

# Check if service is running
echo "🔍 Checking if User Service is running..."
if curl -s "$BASE_URL/actuator/health" > /dev/null; then
    echo -e "${GREEN}✓ User Service is running${NC}"
else
    echo -e "${RED}✗ User Service is not running${NC}"
    echo "Please start the service first: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "🚀 Running User Service Tests"
echo "============================="

# Test 1: Health Check
run_test "Health Check" "GET" "$BASE_URL/actuator/health" "" "200"

# Test 2: Get Public Profiles (should work without auth)
run_test "Get Public Profiles" "GET" "$API_BASE/profiles/public?page=0&size=5" "" "200"

# Test 3: Search Profiles (should work without auth)
run_test "Search Profiles" "GET" "$API_BASE/profiles/search?query=test&page=0&size=5" "" "200"

# Test 4: Get Profile by ID (should work without auth)
run_test "Get Profile by ID" "GET" "$API_BASE/profiles/1" "" "404"

# Test 5: Create Profile (should fail without auth)
run_test "Create Profile (No Auth)" "POST" "$API_BASE/profiles" '{"firstName":"Test","lastName":"User"}' "400"

# Test 6: Get My Profile (should fail without auth)
run_test "Get My Profile (No Auth)" "GET" "$API_BASE/profiles/me" "" "400"

# Test 7: Update Profile (should fail without auth)
run_test "Update Profile (No Auth)" "PUT" "$API_BASE/profiles/me" '{"firstName":"Updated"}' "400"

# Test 8: Delete Profile (should fail without auth)
run_test "Delete Profile (No Auth)" "DELETE" "$API_BASE/profiles/me" "" "400"

# Test 9: Upload Profile Picture (should fail without auth)
run_test "Upload Profile Picture (No Auth)" "POST" "$API_BASE/profiles/me/picture" "" "400"

# Test 10: Get Profile Completion (should fail without auth)
run_test "Get Profile Completion (No Auth)" "GET" "$API_BASE/profiles/me/completion" "" "400"

echo ""
echo "📊 Test Results"
echo "==============="
echo -e "Tests Passed: ${GREEN}$TESTS_PASSED${NC}"
echo -e "Tests Failed: ${RED}$TESTS_FAILED${NC}"
echo -e "Total Tests: $((TESTS_PASSED + TESTS_FAILED))"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "\n${GREEN}🎉 All tests passed! User Service is working correctly.${NC}"
    exit 0
else
    echo -e "\n${RED}❌ Some tests failed. Please check the service configuration.${NC}"
    exit 1
fi
