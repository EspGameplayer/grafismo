import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatchStats } from '../match-stats.model';
import { MatchStatsService } from '../service/match-stats.service';

@Component({
  templateUrl: './match-stats-delete-dialog.component.html',
})
export class MatchStatsDeleteDialogComponent {
  matchStats?: IMatchStats;

  constructor(protected matchStatsService: MatchStatsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matchStatsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
