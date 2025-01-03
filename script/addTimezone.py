import sqlite3
from timezonefinder import TimezoneFinder

# Initialize the timezone finder
tf = TimezoneFinder()

# Connect to the database
conn = sqlite3.connect('airports.db')
cursor = conn.cursor()

# Add the timezone column if it doesn't exist
cursor.execute('ALTER TABLE airports ADD COLUMN timezone TEXT')

# Fetch all airports
cursor.execute('SELECT id, latitude, longitude FROM airports')
airports = cursor.fetchall()

# Function to get timezone from latitude and longitude
def get_timezone(latitude, longitude):
    timezone_str = tf.timezone_at(lat=latitude, lng=longitude)
    if timezone_str is None:
        return 'Unknown'
    return timezone_str

# Update each airport with the timezone information
for airport in airports:
    airport_id, latitude, longitude = airport
    timezone = get_timezone(latitude, longitude)
    cursor.execute('UPDATE airports SET timezone = ? WHERE id = ?', (timezone, airport_id))

# Commit the changes and close the connection
conn.commit()
conn.close()

print("Timezone information updated successfully.")