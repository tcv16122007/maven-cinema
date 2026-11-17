document.addEventListener('DOMContentLoaded', function () {
    const seats = document.querySelectorAll('.seat:not(.occupied)');
    const selectedSeatsEl = document.getElementById('selectedSeats');
    const totalPriceEl = document.getElementById('totalPrice');
    const seatsInput = document.getElementById('seatsInput');
    const submitBtn = document.getElementById('submitBtn');
    const selected = new Set();

    function updateSummary() {
        if (selected.size === 0) {
            selectedSeatsEl.textContent = 'Chưa chọn';
            totalPriceEl.textContent = '0đ';
            submitBtn.disabled = true;
            seatsInput.value = '';
            return;
        }
        const arr = Array.from(selected).sort();
        selectedSeatsEl.textContent = arr.join(', ');

        // Lấy giá vé từ attribute data-price
        const price = parseFloat(document.querySelector('.seat').dataset.price) || 0;
        const total = arr.length * price;
        totalPriceEl.textContent = total.toLocaleString('vi-VN') + 'đ';
        seatsInput.value = arr.join(',');
        submitBtn.disabled = false;
    }

    seats.forEach(seat => {
        seat.addEventListener('click', function () {
            const seatName = this.dataset.seat;
            if (selected.has(seatName)) {
                selected.delete(seatName);
                this.classList.remove('selected');
            } else {
                selected.add(seatName);
                this.classList.add('selected');
            }
            updateSummary();
        });
    });

    document.getElementById('bookingForm').addEventListener('submit', function (e) {
        if (selected.size === 0) {
            e.preventDefault();
            alert('Vui lòng chọn ít nhất 1 ghế');
        }
    });
});