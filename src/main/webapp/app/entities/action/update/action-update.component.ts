import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAction, Action } from '../action.model';
import { ActionService } from '../service/action.service';
import { IMatchStats } from 'app/entities/match-stats/match-stats.model';
import { MatchStatsService } from 'app/entities/match-stats/service/match-stats.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ActionType } from 'app/entities/enumerations/action-type.model';

@Component({
  selector: 'jhi-action-update',
  templateUrl: './action-update.component.html',
})
export class ActionUpdateComponent implements OnInit {
  isSaving = false;
  actionTypeValues = Object.keys(ActionType);

  matchStatsSharedCollection: IMatchStats[] = [];
  playersSharedCollection: IPlayer[] = [];

  editForm = this.fb.group({
    id: [],
    minute: [null, [Validators.min(0)]],
    second: [null, [Validators.min(0), Validators.max(59)]],
    period: [null, [Validators.min(1), Validators.max(5)]],
    type: [null, [Validators.required]],
    status: [null, [Validators.min(0)]],
    matchStats: [],
    players: [],
  });

  constructor(
    protected actionService: ActionService,
    protected matchStatsService: MatchStatsService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ action }) => {
      this.updateForm(action);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const action = this.createFromForm();
    if (action.id !== undefined) {
      this.subscribeToSaveResponse(this.actionService.update(action));
    } else {
      this.subscribeToSaveResponse(this.actionService.create(action));
    }
  }

  trackMatchStatsById(_index: number, item: IMatchStats): number {
    return item.id!;
  }

  trackPlayerById(_index: number, item: IPlayer): number {
    return item.id!;
  }

  getSelectedPlayer(option: IPlayer, selectedVals?: IPlayer[]): IPlayer {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(action: IAction): void {
    this.editForm.patchValue({
      id: action.id,
      minute: action.minute,
      second: action.second,
      period: action.period,
      type: action.type,
      status: action.status,
      matchStats: action.matchStats,
      players: action.players,
    });

    this.matchStatsSharedCollection = this.matchStatsService.addMatchStatsToCollectionIfMissing(
      this.matchStatsSharedCollection,
      action.matchStats
    );
    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing(
      this.playersSharedCollection,
      ...(action.players ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.matchStatsService
      .query()
      .pipe(map((res: HttpResponse<IMatchStats[]>) => res.body ?? []))
      .pipe(
        map((matchStats: IMatchStats[]) =>
          this.matchStatsService.addMatchStatsToCollectionIfMissing(matchStats, this.editForm.get('matchStats')!.value)
        )
      )
      .subscribe((matchStats: IMatchStats[]) => (this.matchStatsSharedCollection = matchStats));

    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(
        map((players: IPlayer[]) =>
          this.playerService.addPlayerToCollectionIfMissing(players, ...(this.editForm.get('players')!.value ?? []))
        )
      )
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }

  protected createFromForm(): IAction {
    return {
      ...new Action(),
      id: this.editForm.get(['id'])!.value,
      minute: this.editForm.get(['minute'])!.value,
      second: this.editForm.get(['second'])!.value,
      period: this.editForm.get(['period'])!.value,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
      matchStats: this.editForm.get(['matchStats'])!.value,
      players: this.editForm.get(['players'])!.value,
    };
  }
}
