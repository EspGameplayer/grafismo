import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatchPlayer, MatchPlayer } from '../match-player.model';
import { MatchPlayerService } from '../service/match-player.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';

@Component({
  selector: 'jhi-match-player-update',
  templateUrl: './match-player-update.component.html',
})
export class MatchPlayerUpdateComponent implements OnInit {
  isSaving = false;

  playersSharedCollection: IPlayer[] = [];
  positionsSharedCollection: IPosition[] = [];

  editForm = this.fb.group({
    id: [],
    shirtNumber: [null, [Validators.min(0)]],
    isWarned: [null, [Validators.min(0), Validators.max(1)]],
    player: [null, Validators.required],
    position: [],
  });

  constructor(
    protected matchPlayerService: MatchPlayerService,
    protected playerService: PlayerService,
    protected positionService: PositionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchPlayer }) => {
      this.updateForm(matchPlayer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matchPlayer = this.createFromForm();
    if (matchPlayer.id !== undefined) {
      this.subscribeToSaveResponse(this.matchPlayerService.update(matchPlayer));
    } else {
      this.subscribeToSaveResponse(this.matchPlayerService.create(matchPlayer));
    }
  }

  trackPlayerById(_index: number, item: IPlayer): number {
    return item.id!;
  }

  trackPositionById(_index: number, item: IPosition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatchPlayer>>): void {
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

  protected updateForm(matchPlayer: IMatchPlayer): void {
    this.editForm.patchValue({
      id: matchPlayer.id,
      shirtNumber: matchPlayer.shirtNumber,
      isWarned: matchPlayer.isWarned,
      player: matchPlayer.player,
      position: matchPlayer.position,
    });

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing(this.playersSharedCollection, matchPlayer.player);
    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing(
      this.positionsSharedCollection,
      matchPlayer.position
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing(players, this.editForm.get('player')!.value)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing(positions, this.editForm.get('position')!.value)
        )
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));
  }

  protected createFromForm(): IMatchPlayer {
    return {
      ...new MatchPlayer(),
      id: this.editForm.get(['id'])!.value,
      shirtNumber: this.editForm.get(['shirtNumber'])!.value,
      isWarned: this.editForm.get(['isWarned'])!.value,
      player: this.editForm.get(['player'])!.value,
      position: this.editForm.get(['position'])!.value,
    };
  }
}
