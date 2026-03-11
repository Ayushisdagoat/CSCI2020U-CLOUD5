games = [
    {"title": "Elden Ring", "genre": "RPG", "platform": "PC", "price": 79.99},
    {"title": "Minecraft", "genre": "Sandbox", "platform": "PC", "price": 39.99}
]

def get_games():
    output = ""
    for g in games:
        output += f"{g['title']} | {g['genre']} | {g['platform']} | ${g['price']}\n"
    return output


if __name__ == "__main__":
    print(get_games())