let stompClient = null

function connectToWebSocket() {
    const socket = new SockJS('/ticket-websocket')
    stompClient = Stomp.over(socket)

    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame)
            $('.connWebSocket').find('i').removeClass('red').addClass('green')

            stompClient.subscribe('/topic/ticket', function (ticketEvent) {
                const ticketEventBody = JSON.parse(ticketEvent.body)
                const ticket = ticketEventBody.ticket
                const action = ticketEventBody.action

                const $ticket = $('#' + ticket.id)
                if (action === 'REMOVE' && $ticket.length !== 0) {
                    $ticket.transition({
                        animation: 'flash',
                        onComplete: function() {
                          $ticket.remove()
                        }
                      }
                    )
                } else if (action === 'INSERT' && $ticket.length === 0) {
                    const ticketItem = '<div class="item" id="'+ticket.id+'">' +
                                       '<div class="content">' +
                                         '<div class="meta">' +
                                           '<span>'+moment(ticket.publishedAt).format("DD-MMM-YYYY HH:mm:ss")+'</span>' +
                                           '<span>Event Type: '+ticket.eventType+'</span>' +
                                         '</div>' +
                                         '<div class="ui divider"></div>' +
                                         '<div class="ui big header">'+ticket.title+'</div>' +
                                       '</div>' +
                                     '</div>'
                    $('#ticketList').prepend(ticketItem)
                    $('#' + ticket.id).transition('glow')
                }
            })
        },
        function() {
            showModal($('.modal.alert'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, ticket-consumer is down or restarting')
            $('.connWebSocket').find('i').removeClass('green').addClass('red')
        }
     )
}

function showModal($modal, header, description, fnApprove) {
    $modal.find('.header').text(header)
    $modal.find('.content').text(description)
    $modal.modal('show')
}

$(function () {
    connectToWebSocket()

    $('.connWebSocket').click(function() {
        connectToWebSocket()
    })
})
